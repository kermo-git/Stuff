#include <iostream>
using namespace std;

template <class E>
class List {
    struct Node {
        E item;
        Node* next;
        Node* prev;

        Node() { next = this; prev = this; }
        Node(Node* prev_node, const E& new_item, Node* next_node) {
            item = new_item;
            link(prev_node, this);
            link(this, next_node);
        }
        void unlink() {
            prev->next = this->next;
            next->prev = this->prev;
        }
    };


    static void link(Node* left, Node* right) {
        left->next = right;
        right->prev = left;
    }


    Node* NULL_NODE = new Node();
    int _size = 0;


    void range_check_end_included(int index) const {
        if (index < 0 || index > _size)
            throw runtime_error("Index out of range.");
    }
    void range_check(int index) const {
        if (index < 0 || index >= _size)
            throw runtime_error("Index out of range.");
    }


    Node* node_at(int index) const {
        Node* ptr = NULL_NODE;
        if (index <= _size >> 1) {
            for (int i = 0; i <= index; ++i)
                ptr = ptr->next;
        } else {
            for (int i = _size-1; i >= index; --i)
                ptr = ptr->prev;
        }
        return ptr;
    }


    void _remove(Node *ptr) {
        ptr->unlink();
        delete ptr;
        _size--;
    }


public:
    List() {}
    List(const List<E>& other) { add_all(other); }
    ~List() {
        if (_size > 0)
            clear();
        delete NULL_NODE;
    }

    int size() const { return _size; }
    bool empty() const { return _size == 0; }
    E& at(int index) {
        range_check(index);
        return node_at(index)->item;
    }
    E& operator[](int index) {
        return at(index);
    }
    E& front() {
        range_check(0);
        return NULL_NODE->next->item;
    }
    E& back() {
        range_check(0);
        return NULL_NODE->prev->item;
    }


    class Iterator {
        Node* node;
    public:
        Iterator() {}
        Iterator(Node* node) { this->node = node; }

        E& operator*() {
            return node->item;
        }
        Iterator& operator++() {
            node = node->next;
            return *this;
        }
        Iterator operator++(int) {
            Iterator tmp = *this;
            node = node->next;
            return tmp;
        }
        Iterator& operator--() {
            node = node->prev;
            return *this;
        }
        Iterator operator--(int) {
            Iterator tmp = *this;
            node = node->prev;
            return tmp;
        }
        bool operator==(const Iterator& other) {
            return node == other.node;
        }
        bool operator!=(const Iterator& other) {
            return node != other.node;
        }
    };
    Iterator begin() { return Iterator(NULL_NODE->next); }
    Iterator end() { return Iterator(NULL_NODE); }


    void push_back(const E& item) {
        new Node(NULL_NODE->prev, item, NULL_NODE); _size++;
    }
    void push_front(const E& item) {
        new Node(NULL_NODE, item, NULL_NODE->next); _size++;
    }
    void insert(int index, const E& item) {
        range_check_end_included(index);
        Node* tmp = node_at(index);
        new Node(tmp->prev, item, tmp);
        _size++;
    }


    void add_all(const List<E>& other) { add_all(_size, other); }
    void add_all(int index, const List<E>& other) {
        range_check_end_included(index);
        Node* this_ptr = node_at(index);
        Node* other_ptr = other.NULL_NODE->next;

        while (other_ptr != other.NULL_NODE) {
            new Node(this_ptr->prev, other_ptr->item, this_ptr);
            other_ptr = other_ptr->next;
        }
        _size += other._size;
    }


    void remove_front() { remove(0); }
    void remove_back() { remove(_size-1); }
    void remove(int index) {
        range_check(index);
        _remove(node_at(index));
    }


    E pop_front() { return pop(0); }
    E pop_back() { return pop(_size-1); }
    E pop(int index) {
        range_check(index);
        Node* node = node_at(index);
        E item = node->item;
        _remove(node);
        return item;
    }


    void clear(int start, int end) {
        range_check(start); range_check(end);

        Node* ptr = node_at(start);
        Node* prev = ptr->prev;

        for (int i = start; i <= end && ptr != NULL_NODE; i++) {
            Node* tmp = ptr;
            ptr = ptr->next;
            delete tmp;
        }
        link(prev, ptr);
        _size -= end - start + 1;
    }
    void clear() {
        if (_size == 0)
            return;
        clear(0, _size-1);
    }


    friend ostream& operator <<(ostream& os, List<E>& list) {
        os << "[";
        if (list._size == 0) { os << "]"; return os; }
        Node* ptr = list.NULL_NODE->next;
        for (int i = 0; i < list._size - 1; i++) {
            os << ptr->item << ", "; ptr = ptr->next;
        }
        os << ptr->item << "]";
        return os;
    }
};


template <class E>
class Vector {
    E* array;
    int _size = 0;
    int capacity;


    void init(int initial_capacity) {
        capacity = initial_capacity;
        array = new E[capacity];
        _size = 0;
    }


    void range_check_end_included(int index) const {
        if (index < 0 || index > _size)
            throw runtime_error("Index out of range.");
    }
    void range_check(int index) const {
        if (index < 0 || index >= _size)
            throw runtime_error("Index out of range.");
    }


    void move(E *new_array, int start_index, int end_index, int shift) {
        for (int i = start_index; i <= end_index; ++i) {
            new_array[i + shift] = array[i];
        }
    }


    void shift_left(int start, int shift) {
        for (int i = start; i < _size; ++i) {
            array[i - shift] = array[i];
        }
    }


    void shift_right(int start, int shift) {
        for (int i = _size-1; i >= start; --i) {
            array[i + shift] = array[i];
        }
    }


    void create_gap(int start, int length) {
        if (this->_size + length > capacity) {
            capacity = (_size + length) << 1;
            E *new_array = new E[capacity];

            move(new_array, 0, start-1, 0);
            move(new_array, start, _size-1, length);

            delete[] array;
            array = new_array;
        } else {
            shift_right(start, length);
        }
    }


    void remove_gap(int start, int length) {
        if (_size - length <= capacity >> 2) {
            capacity >>= 1;
            E *new_array = new E[capacity];

            move(new_array, 0, start-1, 0);
            move(new_array, start+length, _size-1, -length);

            delete[] array;
            array = new_array;
        } else {
            shift_left(start+length, length);
        }
    }

public:
    Vector(int initial_capacity) { init(initial_capacity); }
    Vector() { init(10); }
    Vector(const Vector<E>& other) {
        init(other.size());
        add_all(other);
    }
    ~Vector() { delete[] array; }


    


    int size() const { return _size; }
    bool empty() const { return _size == 0; }
    E& at(int index) {
        range_check(index);
        return array[index];
    }
    E& operator[](int index) {
        return at(index);
    }
    E& front() {
        range_check(0);
        return array[0];
    }
    E& back() {
        range_check(0);
        return array[_size - 1];
    }


    typedef E* Iterator;
    Iterator begin() { return array; }
    Iterator end() { return array + _size; }


    void push_back(const E& data) { insert(_size, data); }
    void push_front(const E& data) { insert(0, data); }
    void insert(int index, const E& data) {
        range_check_end_included(index);
        create_gap(index, 1);
        array[index] = data;
        _size++;
    }


    void add_all(const Vector<E>& other) { add_all(_size, other); }
    void add_all(int index, const Vector<E>& other) {
        range_check_end_included(index);
        E* it = other.array;
        create_gap(index, other._size);
        E *ptr = array + index;
        for (int i = 0; i < other._size; i++) {
            *ptr = *it;
            ptr++; it++;
        }
        _size += other._size;
    }


    void remove_front() { remove(0); }
    void remove_back() { remove(_size - 1); }
    void remove(int index) {
        range_check(index);
        remove_gap(index, 1);
        _size--;
    }


    E pop_front() { return pop(0); }
    E pop_back() { return pop(_size - 1); }
    E pop(int index) {
        range_check(index);
        E item = array[index];
        remove_gap(index, 1);
        _size--;
        return item;
    }


    void clear(int start, int end) {
        range_check(start); range_check(end);
        int gap_length = end - start + 1;
        remove_gap(start, gap_length);
        _size -= gap_length;
    }
    void clear() {
        if (_size == 0)
            return;
        clear(0, _size-1);
    }
    
    
    friend ostream& operator <<(ostream& os, Vector<E>& c) {
        os << "[";
        if (c._size == 0) { os << "]"; return os; }
        E* ptr = c.array;
        for (int i = 0; i < c._size-1; ++i) {
            os << *(ptr) << ", "; ++ptr;
        }
        os << *(ptr) << "]";
        return os;
    }
};
