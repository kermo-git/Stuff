#include <iostream>
#include <sstream>
using namespace std;


template <class E, class I>
class Collection;

template <class E>
class ListIterator;

template <class E>
class List;

template <class E>
class ArrayIterator;

template <class E>
class Vector;


template <class E, class IT>
class Collection {
protected:
    int _size = 0;
    void range_check(int index, bool including_end) const {
        if (index < 0 || (including_end)? (index > _size) : (index >= _size))
            throw runtime_error("Index out of range.");
    }

public:
    typedef IT Iterator;
    typedef Collection<E, IT> Sequence;

    int size() const { return _size; }
    bool empty() const { return _size == 0; }
    virtual E& at(int index)=0;
    E& operator [](int index) { return at(index); };
    virtual E& front() { return at(0); };
    virtual E& back() { return at(_size - 1); };

    void push_back(const E& data) { insert(data, _size); }
    void push_front(const E& data) { insert(data, 0); }
    virtual void insert(const E& data, int index)=0;

    virtual IT iterator_front() const { return iterator_at(0); }
    virtual IT iterator_back() const { return iterator_at(_size - 1); }
    virtual IT iterator_at(int index) const =0;

    void remove_front() { remove(0); }
    void remove_back() { remove(_size-1); }
    virtual void remove(int index)=0;

    E pop_front() { return pop(0); }
    E pop_back() { return pop(_size-1); }
    virtual E pop(int index)=0;

    void clear() { clear(0, _size-1); }
    virtual void clear(int start, int end)=0;

    void sort() { sort(iterator_front(), iterator_back()); }

    E max() {
        IT it = iterator_front();
        E result = *(it);
        for (; !it.empty(); ++it) {
            if (*(it) > result)
                result = *(it);
        }
        return result;
    }

    E min() {
        IT it = iterator_front();
        E result = *(it);
        for (; !it.empty(); ++it) {
            if (*(it) < result)
                result = *(it);
        }
        return result;
    }

    E sum() {
        IT it = iterator_front();
        E result = *(it); ++it;
        for (; !it.empty(); ++it) {
            result += *(it);
        }
        return result;
    }

    E product() {
        IT it = iterator_front();
        E result = *(it); ++it;
        for (; !it.empty(); ++it) {
            result *= *(it);
        }
        return result;
    }

    virtual void reverse() {
        IT start = iterator_front();
        IT end = iterator_back();
        while (true) {
            E tmp = *(start);
            *(start) = *(end);
            *(end) = tmp;
            if (++start == end) break;
            --end;
        }
    }

    friend ostream& operator <<(ostream& os, Collection& c) {
        os << "[";
        if (c.size() == 0) { os << "]"; return os; }
        IT it = c.iterator_front();
        for (int i = 0; i < c.size()-1; i++) {
            os << *(it) << ", "; it++;
        }
        os << *(it) << "]";
        return os;
    }

private:

    IT partition(IT start, E& pivot, IT end) {
        IT left = start;
        while (true) {
            if (left == end) return left;
            if (*(left) >= pivot) break;
            ++left;
        }
        IT right = end;
        while (true) {
            if (right == left) return right;
            if (*(right) < pivot) break;
            --right;
        }
        E tmp = *(left);
        *(left) = *(right);
        *(right) = tmp;
        --right;
        if (left == right) return left;
        return partition(++left, pivot, right);
    }


    void sort(IT start, IT end) {
        if (start == end)
            return;

        E pivot = *(start);
        IT split = partition(start, pivot, end);

        if (split == end) { sort(start, --split); }
        else if (split == start) { sort(++split, end); }
        else if (*(split) < pivot){
            sort(start, split);
            sort(++split, end);
        } else {
            sort(start, --split);
            sort(split, end);
        }
    }
};


template <class E>
class ListIterator {
    typename List<E>::Node *node;
public:
    ListIterator() {}
    ListIterator(typename List<E>::Node *node_ptr) { node = node_ptr; }

    bool has_next() { return node->next != NULL; }
    bool has_prev() { return node->prev != NULL; }
    bool empty() { return node == NULL; }

    ListIterator& operator ++() {
        node = node->next;
        return *this;
    }
    ListIterator& operator --() {
        node = node->prev;
        return *this;
    }
    ListIterator operator ++(int) {
        ListIterator tmp = *this;
        node = node->next;
        return tmp;
    }
    ListIterator operator --(int) {
        ListIterator tmp = *this;
        node = node->prev;
        return tmp;
    }
    E& operator *() {
        if (node == NULL)
            throw runtime_error("Empty iterator.");
        return node->item;
    }
    bool operator ==(const ListIterator& other) { return node == other.node; }
    bool operator !=(const ListIterator& other) { return node != other.node; }
};


template <class E>
class List : public Collection<E, ListIterator<E> > {
    friend class ListIterator<E>;
    struct Node {
        E item;
        Node *next = NULL;
        Node *prev = NULL;


        Node(E new_item) {
            item = new_item;
        }
        Node(Node *prev_node, const E& new_item, Node *next_node) {
            item = new_item;
            link_behind(prev_node);
            link_front(next_node);
        }
        Node(Node *prev_node, const E& new_item) {
            item = new_item;
            link_behind(prev_node);
        }
        Node(const E& new_item, Node *next_node) {
            item = new_item;
            link_front(next_node);
        }
        ~Node() {
            if (prev != NULL) prev->next = this->next;
            if (next != NULL) next->prev = this->prev;
        };


        void link_front(Node *other) {
            next = other;
            other->prev = this;
        }
        void link_behind(Node *other) {
            prev = other;
            other->next = this;
        }
    };


    Node *front_node;
    Node *back_node;


    bool the_first(const E& item) {
        if (this->_size == 0) {
            front_node = new Node(item);
            back_node = front_node;
            return true;
        }
        return false;
    }
    void _remove_front() {
        Node *temp = front_node;
        front_node = front_node->next;
        delete temp;
    }
    void _remove_back() {
        Node *temp = back_node;
        back_node = back_node->prev;
        delete temp;
    }
    void _push_front(const E& item) {
        front_node = new Node(item, front_node);
    }
    void _push_back(const E& item) {
        back_node = new Node(back_node, item);
    }
    void _insert(const E& item, Node *right) {
        new Node(right->prev, item, right);
    }


    Node *node_at(int index) const {
        Node *ptr;
        if (index < this->_size/2) {
            ptr = front_node;
            for (int i = 0; i < index; i++)
                ptr = ptr->next;
        } else {
            ptr = back_node;
            for (int i = this->_size-1; i > index; i--)
                ptr = ptr->prev;
        }
        return ptr;
    }

public:
    List() {}
    List(const List<E>& other) {
        add_all<List<E> >(other);
    }
    ~List() { Collection<E, ListIterator<E>>::clear(); }


    E& at(int index) {
        this->range_check(index, false);
        return node_at(index)->item;
    }
    E& front() { return front_node->item; }
    E& back() { return back_node->item; }


    ListIterator<E> iterator_front() const { return ListIterator<E>(front_node); }
    ListIterator<E> iterator_back() const { return ListIterator<E>(back_node); }
    ListIterator<E> iterator_at(int index) const {
        this->range_check(index, false);
        return ListIterator<E>(node_at(index));
    }


    void insert(const E& c, int index) {
        this->range_check(index, true);
        if (!the_first(c)) {
            if (index == 0)
                _push_front(c);
            else if (index == this->_size)
                _push_back(c);
            else
                _insert(c, node_at(index));
        }
        this->_size++;
    }


    template <class Sequence>
    void add_all(const Sequence& c) { add_all<Sequence>(c, this->_size); }


    template <class Sequence>
    void add_all(const Sequence& c, int index) {
        this->range_check(index, true);
        typename Sequence::Iterator it = c.iterator_front();

        if (the_first(*(it))) it++;
        if (index == this->_size) {
            for (; !it.empty(); it++) { _push_back(*(it));}
        }
        if (index == 0) {
            Node *ptr = new Node(*(it), front_node); it++;
            for (; !it.empty(); it++) {
                _insert(*(it), front_node);
            }
            front_node = ptr;
        }
        else {
            Node *ptr = node_at(index);
            for (; !it.empty(); it++) {
                _insert(*(it), ptr);
            }
        }
        this->_size += c.size();
    }


    void remove(int index) {
        this->range_check(index, false);
        if (index == 0)
            _remove_front();
        else if (index == this->_size - 1)
            _remove_back();
        else
            delete node_at(index);
        this->_size--;
    }


    E pop(int index) {
        this->range_check(index, false);
        E item;
        if (index == 0) {
            item = front_node->item;
            _remove_front();
        }
        else if (index == this->_size-1) {
            item = back_node->item;
            _remove_back();
        }
        else {
            Node *ptr = node_at(index);
            item = ptr->item;
            delete ptr;
        }
        this->_size--;
        return item;
    }


    void clear(int start, int end) {
        this->range_check(start, false);
        this->range_check(end, false);
        Node *ptr = node_at(start);
        if (end == this->_size-1)
            back_node = ptr->prev;

        for (int i = start; i <= end; i++) {
            Node *temp = ptr;
            ptr = ptr->next;
            delete temp;
        }
        if (start == 0)
            front_node = ptr;

        this->_size -= end - start + 1;
    }
};


template <class E>
class ArrayIterator {
    E *ptr;
    int index;
    int size;
public:
    ArrayIterator() {}
    ArrayIterator(E *element_ptr, int element_index, int array_size) {
        ptr = element_ptr;
        index = element_index;
        size = array_size;
    }
    bool has_next() { return index < size-1; }
    bool has_prev() { return index > 0; }
    bool empty() { return index < 0 || index >= size; }

    ArrayIterator operator ++() {
        ptr++; index++;
        return *this;
    }
    ArrayIterator operator --() {
        ptr--; index--;
        return *this;
    }
    ArrayIterator operator ++(int) {
        ArrayIterator tmp = *this;
        ptr++; index++;
        return tmp;
    }
    ArrayIterator operator --(int) {
        ArrayIterator tmp = *this;
        ptr--; index--;
        return tmp;
    }
    E& operator *() {
        if (index < 0 || index >= size)
            throw runtime_error("Empty iterator.");
        return *(ptr);
    }
    bool operator ==(const ArrayIterator& other) { return ptr == other.ptr; }
    bool operator !=(const ArrayIterator& other) { return ptr != other.ptr; }
};


template <class E>
class Vector: public Collection<E, ArrayIterator<E> > {
    E *array;
    int capacity;
    const int DEFAULT_INITIAL_CAPACITY = 10;


    void init(int initial_capacity) {
        capacity = initial_capacity;
        array = new E[capacity];
        this->_size = 0;
    }


    static void move(E *from_array, E *to_array, int start_index, int end_index, int shift) {
        if (shift < 0) {
            for (int i = start_index; i <= end_index; i++) {
                to_array[i + shift] = from_array[i];
            }
        } else {
            for (int i = end_index; i >= start_index; i--) {
                to_array[i + shift] = from_array[i];
            }
        }
    }
    void create_gap(int start, int length) {
        if (this->_size + length > capacity) {
            capacity = (this->_size + length)*2;
            E *new_array = new E[capacity];

            move(array, new_array, 0, start-1, 0);
            move(array, new_array, start, this->_size-1, length);

            delete[] array;
            array = new_array;
        } else {
            move(array, array, start, this->_size-1, length);
        }
    }
    void remove_gap(int start, int length) {
        if (this->_size - length <= capacity/4) {
            capacity /= 2;
            E *new_array = new E[capacity];

            move(array, new_array, 0, start-1, 0);
            move(array, new_array, start+length, this->_size-1, -length);

            delete[] array;
            array = new_array;
        } else {
            move(array, array, start+length, this->_size-1, -length);
        }
    }

public:
    Vector(int initial_capacity) { init(initial_capacity); }
    Vector() { init(DEFAULT_INITIAL_CAPACITY); }
    Vector(const Vector<E>& other) {
        init(other.size());
        add_all<Vector<E> >(other);
    }
    ~Vector() { delete[] array; }


    E& at(int index) {
        this->range_check(index, false);
        return array[index];
    }
    E& front() { return array[0]; }
    E& back() { return array[this->_size - 1]; }


    ArrayIterator<E> iterator_at(int index) const {
        this->range_check(index, false);
        return ArrayIterator<E>(array + index, index, this->_size);
    }


    void insert(const E& c, int index) {
        this->range_check(index, true);
        create_gap(index, 1);
        array[index] = c;
        this->_size++;
    }


    template <class Sequence>
    void add_all(const Sequence& c) { add_all<Sequence>(c, this->_size); }


    template <class Sequence>
    void add_all(const Sequence& c, int index) {
        this->range_check(index, true);
        typename Sequence::Iterator it = c.iterator_front();
        create_gap(index, c.size());
        E *ptr = array + index;
        while (!it.empty()) {
            *ptr = *it;
            ptr++; it++;
        }
        this->_size += c.size();
    }


    void remove(int index) {
        this->range_check(index, false);
        remove_gap(index, 1);
        this->_size--;
    }


    E pop(int index) {
        this->range_check(index, false);
        E item = array[index];
        remove_gap(index, 1);
        this->_size--;
        return item;
    }


    void clear(int start, int end) {
        this->range_check(start, false);
        this->range_check(end, false);
        int gap_length = end - start + 1;
        remove_gap(start, gap_length);
        this->_size -= gap_length;
    }
};


int main() {
    typedef Vector<int> lst_2;
    typedef List<int> lst_1;
    lst_1 list;

    list.push_back(5);
    list.push_back(6);
    list.push_back(14); // Grows 2X if Vector(2)
    list.push_back(4);
    list.push_back(7); // Grows 2X if Vector(2)
    list.push_back(19);
    list.push_back(2);

    cout << "Expected: [5, 6, 14, 4, 7, 19, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.push_front(3);
    list.push_front(1); // Grows 2X if Vector(2)

    cout << "Expected: [1, 3, 5, 6, 14, 4, 7, 19, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.push_back(9);
    list.push_back(12);
    list.push_back(6);
    list.insert(8, 5);

    cout << "Expected: [1, 3, 5, 6, 14, 8, 4, 7, 19, 2, 9, 12, 6]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.push_back(8);
    list.push_back(2);
    list.insert(13, 6);
    list.insert(11, 11); // Grows 2X if Vector(2)

    cout << "Expected: [1, 3, 5, 6, 14, 8, 13, 4, 7, 19, 2, 11, 9, 12, 6, 8, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.push_back(10);
    list.push_back(10);
    list.push_back(10);
    list.remove_back();
    list.remove_back();
    list.remove_back();

    list.push_front(10);
    list.push_front(10);
    list.push_front(10);
    list.remove_front();
    list.remove_front();
    list.remove_front();

    cout << "Expected: [1, 3, 5, 6, 14, 8, 13, 4, 7, 19, 2, 11, 9, 12, 6, 8, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.remove(10);
    list.remove(1); // Shrinks 2X if Vector(2)
    list.remove(12);

    cout << "Expected: [1, 5, 6, 14, 8, 13, 4, 7, 19, 11, 9, 12, 8, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    list.sort();
    cout << "Sorted:   " << list << endl;
    list.reverse();
    cout << "Reversed: " << list << endl << endl;

    cout << "Expected size: 14" << endl;
    cout << "Actual size:   " << list.size() << endl << endl;

    int front = list.pop_front();
    cout << "Expected to pop 19, actually popped " << front << endl;
    cout << "Expected: [14, 13, 12, 11, 9, 8, 8, 7, 6, 5, 4, 2, 1]" << endl;
    cout << "Actual:   " << list << endl << endl;

    int back = list.pop_back();
    cout << "Expected to pop 1, actually popped " << back << endl;
    cout << "Expected: [14, 13, 12, 11, 9, 8, 8, 7, 6, 5, 4, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    int middle = list.pop(4);
    cout << "Expected to pop 9, actually popped " << middle << endl;
    cout << "Expected: [14, 13, 12, 11, 8, 8, 7, 6, 5, 4, 2]" << endl;
    cout << "Actual:   " << list << endl << endl;

    lst_2 extra_1;
    extra_1.push_back(21);
    extra_1.push_back(24);
    extra_1.push_back(22);
    extra_1.push_back(23);

    list.add_all(extra_1);
    cout << "Expected: [14, 13, 12, 11, 8, 8, 7, 6, 5, 4, 2, 21, 24, 22, 23]" << endl;
    cout << "Actual:   " << list << endl << endl;

    lst_2 extra_2;
    extra_2.push_back(25);
    extra_2.push_back(28);
    extra_2.push_back(26);
    extra_2.push_back(27);

    list.add_all(extra_2, 0);
    cout << "Expected: [25, 28, 26, 27, 14, 13, 12, 11, 8, 8, 7, 6, 5, 4, 2, 21, 24, 22, 23]" << endl;
    cout << "Actual:   " << list << endl << endl;

    lst_2 extra_3;
    extra_3.push_back(30);
    extra_3.push_back(39);
    extra_3.push_back(37);
    extra_3.push_back(31);

    list.add_all(extra_3, 7);
    cout << "Expected: [25, 28, 26, 27, 14, 13, 12, 30, 39, 37, 31, 11, 8, 8, 7, 6, 5, 4, 2, 21, 24, 22, 23]" << endl;
    cout << "Actual:   " << list << endl << endl;

    cout << "Expected max 39, actual max " << list.max() << "." << endl;
    cout << "Expected min 2, actual min " << list.min() << "." << endl << endl;

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

    cout << "Expected sum 60, actual sum " << list.sum() << "." << endl;
    cout << "Expected product 75600, actual product " << list.product() << "." << endl << endl;

    cout << "EOF" << endl;
	return 0;
}