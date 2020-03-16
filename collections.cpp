#include <iostream>
using namespace std;

template <class E>
class Vector {
    E *array;
    int _size = 0;
    int capacity;
    const int DEFAULT_INITIAL_CAPACITY = 10;


    void init(int initial_capacity) {
        capacity = initial_capacity;
        array = new E[capacity];
        _size = 0;
    }


    void range_check(int index, bool including_end) const {
        if (index < 0 || (including_end)? (index > _size) : (index >= _size)) {
            delete[] array;
            throw runtime_error("Index out of range.");
        }
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
    Vector() { init(DEFAULT_INITIAL_CAPACITY); }
    Vector(const Vector<E>& other) {
        init(other.size());
        add_all(other);
    }
    ~Vector() { delete[] array; }


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


    int size() const { return _size; }
    bool empty() const { return _size == 0; }
    E& operator [](int index) {
        range_check(index, false);
        return array[index];
    }
    E& first() { return array[0]; }
    E& last() { return array[_size - 1]; }


    void add(const E& data) { add(data, _size); }
    void add_front(const E& data) { add(data, 0); }
    void add(const E& data, int index) {
        range_check(index, true);
        create_gap(index, 1);
        array[index] = data;
        _size++;
    }


    void add_all(const Vector<E>& other) { add_all(_size, other); }
    void add_all(int index, const Vector<E>& other) {
        range_check(index, true);
        E* it = other.array;
        create_gap(index, other._size);
        E *ptr = array + index;
        for (int i = 0; i < other._size; i++) {
            *ptr = *it;
            ptr++; it++;
        }
        _size += other._size;
    }


    void remove_first() { remove(0); }
    void remove_last() { remove(_size - 1); }
    void remove(int index) {
        range_check(index, false);
        remove_gap(index, 1);
        _size--;
    }


    E pop_first() { return pop(0); }
    E pop_last() { return pop(_size - 1); }
    E pop(int index) {
        range_check(index, false);
        E item = array[index];
        remove_gap(index, 1);
        _size--;
        return item;
    }


    void clear() { clear(0, _size-1); }
    void clear(int start, int end) {
        range_check(start, false);
        range_check(end, false);
        int gap_length = end - start + 1;
        remove_gap(start, gap_length);
        _size -= gap_length;
    }
};

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


    Node* node_at(int index) const {
        if (index < 0 || index >= _size)
            throw runtime_error("Index out of range.");

        Node* ptr;
        if (index <= _size >> 1) {
            ptr = NULL_NODE->next;
            for (int i = 0; i < index; ++i)
                ptr = ptr->next;
        } else {
            ptr = NULL_NODE->prev;
            for (int i = _size-1; i > index; --i)
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
    E& operator [](int index) {
        return node_at(index)->item;
    }
    E& first() {
        return NULL_NODE->next->item;
    }
    E& last() {
        return NULL_NODE->prev->item;
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



    void add(const E& item) {
        new Node(NULL_NODE->prev, item, NULL_NODE); _size++;
    }
    void add_first(const E& item) {
        new Node(NULL_NODE, item, NULL_NODE->next); _size++;
    }
    void add(int index, const E& item) {
        if (index == _size)
            new Node(NULL_NODE->prev, item, NULL_NODE);
        else {
            Node* tmp = node_at(index);
            new Node(tmp->prev, item, tmp);
        }
        _size++;
    }


    void add_all(const List<E>& other) { add_all(_size, other); }
    void add_all(int index, const List<E>& other) {
        Node* this_ptr = (index == _size) ? NULL_NODE : node_at(index);
        Node* other_ptr = other.NULL_NODE->next;

        while (other_ptr != other.NULL_NODE) {
            new Node(this_ptr->prev, other_ptr->item, this_ptr);
            other_ptr = other_ptr->next;
        }
        _size += other._size;
    }


    void remove_first() {
        _remove(NULL_NODE->next);
    }
    void remove_last() {
        _remove(NULL_NODE->prev);
    }
    void remove(int index) {
        _remove(node_at(index));
    }


    E pop_first() {
        E item = NULL_NODE->next->item;
        _remove(NULL_NODE->next);
        return item;
    }
    E pop_last() {
        E item = NULL_NODE->prev->item;
        _remove(NULL_NODE->prev);
        return item;
    }
    E pop(int index) {
        Node* node = node_at(index);
        E item = node->item;
        _remove(node);
        return item;
    }


    void clear() { clear(0, _size-1); }
    void clear(int start, int end) {
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
};

int list_main() {
    List<string>* test_list = new List<string>();
    delete test_list;

    List<string>* str_list = new List<string>();
    str_list->add("lemon");
    str_list->add("lime");
    str_list->add("banana");
    cout << *(str_list) << endl;
    delete str_list;

    List<int> list;

    list.add(5);
    list.add(6);
    list.add(14); // Grows 2X if Vector(2)
    list.add(4);
    list.add(7); // Grows 2X if Vector(2)
    list.add(19);
    list.add(2);

    cout << "Expected: [5, 6, 14, 4, 7, 19, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.add_first(3);
    list.add_first(1); // Grows 2X if Vector(2)

    cout << "Expected: [1, 3, 5, 6, 14, 4, 7, 19, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.add(9);
    list.add(12);
    list.add(6);
    list.add(5, 8);

    cout << "Expected: [1, 3, 5, 6, 14, 8, 4, 7, 19, 2, 9, 12, 6]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.add(8);
    list.add(2);
    list.add(6, 13);
    list.add(11, 11); // Grows 2X if Vector(2)

    cout << "Expected: [1, 3, 5, 6, 14, 8, 13, 4, 7, 19, 2, 11, 9, 12, 6, 8, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.add(10);
    list.add(10);
    list.add(10);
    list.remove_last();
    list.remove_last();
    list.remove_last();

    list.add_first(10);
    list.add_first(10);
    list.add_first(10);
    list.remove_first();
    list.remove_first();
    list.remove_first();

    cout << "Expected: [1, 3, 5, 6, 14, 8, 13, 4, 7, 19, 2, 11, 9, 12, 6, 8, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.remove(10);
    list.remove(1); // Shrinks 2X if Vector(2)
    list.remove(12);

    cout << "Expected: [1, 5, 6, 14, 8, 13, 4, 7, 19, 11, 9, 12, 8, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    //list.sort();
    //cout << "Sorted:   " << list << endl;
    //list.reverse();
    //cout << "Reversed: " << list << endl << endl;

    cout << "Expected size: 14" << endl;
    cout << "Actual size:   " << list.size() << endl << endl;

    int front = list.pop_first();
    cout << "Expected to pop 19, actually popped " << front << endl;
    cout << "Expected: [14, 13, 12, 11, 9, 8, 8, 7, 6, 5, 4, 2, 1]" << endl;
    cout << "Actual:   " << list << endl << endl;

    int back = list.pop_last();
    cout << "Expected to pop 1, actually popped " << back << endl;
    cout << "Expected: [14, 13, 12, 11, 9, 8, 8, 7, 6, 5, 4, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    int middle = list.pop(4);
    cout << "Expected to pop 9, actually popped " << middle << endl;
    cout << "Expected: [14, 13, 12, 11, 8, 8, 7, 6, 5, 4, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    List<int> extra_1;
    extra_1.add(21);
    extra_1.add(24);
    extra_1.add(22);
    extra_1.add(23);

    list.add_all(extra_1);
    cout << "Expected: [14, 13, 12, 11, 8, 8, 7, 6, 5, 4, 2, 21, 24, 22, 23]" << endl;
    cout << "Actual:   " << list << endl << endl;

    List<int> extra_2;
    extra_2.add(25);
    extra_2.add(28);
    extra_2.add(26);
    extra_2.add(27);

    list.add_all(0, extra_2);
    cout << "Expected: [25, 28, 26, 27, 14, 13, 12, 11, 8, 8, 7, 6, 5, 4, 2, 21, 24, 22, 23]" << endl;
    cout << "Actual:   " << list << endl << endl;

    List<int> extra_3;
    extra_3.add(30);
    extra_3.add(39);
    extra_3.add(37);
    extra_3.add(31);

    list.add_all(7, extra_3);
    cout << "Expected: [25, 28, 26, 27, 14, 13, 12, 30, 39, 37, 31, 11, 8, 8, 7, 6, 5, 4, 2, 21, 24, 22, 23]" << endl;
    cout << "Actual:   " << list << endl << endl;

    //cout << "Expected max 39, actual max " << list.max() << "." << endl;
    //cout << "Expected min 2, actual min " << list.min() << "." << endl << endl;

    list.clear(0, 5);
    cout << "Expected: [12, 30, 39, 37, 31, 11, 8, 8, 7, 6, 5, 4, 2, 21, 24, 22, 23]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.clear(11, 16);
    cout << "Expected: [12, 30, 39, 37, 31, 11, 8, 8, 7, 6, 5]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.clear(2, 7);
    cout << "Expected: [12, 30, 7, 6, 5]" << endl;
    cout << "Actual:   " << list << endl << endl;

    cout << "Expected size: 5" << endl;
    cout << "Actual size:   " << list.size() << endl << endl;

    //cout << "Expected sum 60, actual sum " << list.sum() << "." << endl;
    //cout << "Expected product 75600, actual product " << list.product() << "." << endl << endl;

    cout << "EOF" << endl;
    return 0;
}
