#include <iostream>

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
        static void link(Node* left, Node* right) {
            left->next = right;
            right->prev = left;
        }
        void unlink() {
            prev->next = this->next;
            next->prev = this->prev;
        }
    };


    Node* NULL_NODE = new Node();
    int _size = 0;


    void range_check_end_included(int index) const {
        if (index < 0 || index > _size)
            throw std::runtime_error("Index out of range.");
    }
    void range_check(int index) const {
        if (index < 0 || index >= _size)
            throw std::runtime_error("Index out of range.");
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
    List(const List<E>& other) {
        add_all(0, other);
    }
    ~List() {
        clear(); delete NULL_NODE;
    }

    int size() const { return _size; }
    bool empty() const { return _size == 0; }


    E& operator[](int index) {
        range_check(index);
        return node_at(index)->item;
    }
    void operator+=(const List<E>& other) {
        add_all(_size, other);
    }
    friend List<E> operator+(const List<E>& left, const List<E>& right) {
        List<E> result = left;
        result += right;
        return result;
    }
    void operator=(const List<E>& other) {
        clear(); add_all(0, other);
    }
    friend bool operator==(const List<E>& left, const List<E>& right) {
        if (left._size == right._size) {
            Node* node_1 = left.NULL_NODE->next;
            Node* node_2 = right.NULL_NODE->next;
            while (node_1 != left.NULL_NODE) {
                if (!(node_1->item == node_2->item))
                    return false;
                node_1 = node_1->next;
                node_2 = node_2->next;
            }
            return true;
        }
        return false;
    }
    friend bool operator!=(const List<E>& left, const List<E>& right) {
        return !(left == right);
    }
    friend std::ostream& operator<<(std::ostream& os, List<E>& list) {
        os << "[";
        if (list._size == 0) { os << "]"; return os; }
        Node* ptr = list.NULL_NODE->next;
        for (int i = 0; i < list._size - 1; i++) {
            os << ptr->item << ", "; ptr = ptr->next;
        }
        os << ptr->item << "]";
        return os;
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


    E& front() { return this[0]; }
    E& back() { return this[_size-1]; }
    E& at(int index) { return this[index]; }


    void push_back(const E& item) { insert(_size, item); }
    void push_front(const E& item) { insert(0, item); }
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


    List<E> sub_list(int start, int end) {
        range_check(start); range_check(end);

        Node* node = node_at(start);
        Node* new_null = new Node();
        int new_size = end - start + 1;

        for (int i = 0; i < new_size; ++i) {
            new Node(new_null->prev, node->item, new_null);
            node = node->next;
        }
        List<E> result;
        result._size = new_size;
        result.NULL_NODE = new_null;

        return result;
    }


    void clear(int start, int end) {
        range_check(start); range_check(end);
        Node* ptr = node_at(start);
        Node* prev = ptr->prev;

        for (int i = start; i <= end; i++) {
            Node* tmp = ptr;
            ptr = ptr->next;
            delete tmp;
        }
        Node::link(prev, ptr);
        _size -= end - start + 1;
    }
    void clear() {
        if (_size == 0)
            return;
        clear(0, _size-1);
    }
};
