#ifndef VECTOR_H
#define VECTOR_H
#include <iostream>


template <class E>
class Vector {
    E* array;
    int _size = 0;
    int capacity;


    void init(int initial_capacity) {
        capacity = initial_capacity;
        array = new E[capacity];
    }


    void range_check_end_included(int index) const {
        if (index < 0 || index > _size)
            throw std::runtime_error("Index out of range.");
    }
    void range_check(int index) const {
        if (index < 0 || index >= _size)
            throw std::runtime_error("Index out of range.");
    }

    
    void create_gap(int start, int length) {
        if (_size + length > capacity) {
            capacity = (_size + length) << 1;
            E *new_array = new E[capacity];

            for (int i = 0; i <= start-1; ++i)
                new_array[i] = array[i];
            for (int i = start; i <= _size-1; ++i)
                new_array[i + length] = array[i];

            delete[] array;
            array = new_array;
        } else {
            for (int i = _size-1; i >= start; --i) {
                array[i + length] = array[i];
            }
        }
    }
    
    
    void remove_gap(int start, int length) {
        if (_size - length <= capacity >> 2) {
            capacity >>= 1;
            E *new_array = new E[capacity];

            for (int i = 0; i <= start-1; ++i)
                new_array[i] = array[i];
            for (int i = start+length; i <= _size-1; ++i)
                new_array[i - length] = array[i];

            delete[] array;
            array = new_array;
        } else {
            for (int i = start+length; i < _size; ++i) {
                array[i - length] = array[i];
            }
        }
    }


public:
    Vector() { init(10); }
    Vector(int initial_capacity) {
        init(initial_capacity);
    }
    Vector(const Vector<E>& other) {
        init(other.capacity);
        add_all(other);
    }
    ~Vector() { delete[] array; }


    int size() const { return _size; }
    bool empty() const { return _size == 0; }


    E& operator[](int index) {
        range_check(index);
        return array[index];
    }
    void operator+=(const Vector<E>& other) {
        add_all(_size, other);
    }
    void operator=(const Vector<E>& other) {
        clear(); add_all(0, other);
    }
    
    
    friend Vector<E> operator+(const Vector<E>& left, const Vector<E>& right) {
        Vector<E> result = left;
        result += right;
        return result;
    }
    
    
    friend bool operator==(const Vector<E>& left, const Vector<E>& right) {
        if (left._size == right._size) {
            E* it_1 = left.array;
            E* it_2 = right.array;
            for (int i = 0; i < left._size; i++) {
                if (!(*it_1 == *it_2))
                    return false;
                it_1++; it_2++;
            }
            return true;
        }
        return false;
    }
    friend bool operator!=(const Vector<E>& left, const Vector<E>& right) {
        return !(left == right);
    }
    
    
    friend std::ostream& operator<<(std::ostream& os, Vector<E>& c) {
        os << "[";
        if (c._size == 0) { os << "]"; return os; }
        E* ptr = c.array;
        for (int i = 0; i < c._size-1; ++i) {
            os << *(ptr) << ", "; ++ptr;
        }
        os << *(ptr) << "]";
        return os;
    }


    typedef E* Iterator;
    Iterator begin() { return array; }
    Iterator end() { return array + _size; }


    E& front() { return this[0]; }
    E& back() { return this[_size-1]; }
    E& at(int index) { return this[index]; }
    
    
    bool contains(const E& value) {
        for (E* ptr = array; ptr != array + _size; ++ptr) {
            if (*ptr == value)
                return true;
        }
        return false;
    }
    int count(const E& value) {
        int count = 0;
        for (E* ptr = array; ptr != array + _size; ++ptr) {
            if (*ptr == value)
                ++count;
        }
        return count;
    }


    void push_back(const E& data) { insert(_size, data); }
    void push_front(const E& data) { insert(0, data); }
    void insert(int index, const E& data) {
        range_check_end_included(index);
        create_gap(index, 1);
        array[index] = data; _size++;
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
    bool remove(const E& value) {
        E* ptr = array;

        for (int i = 0; i < _size; ++i) {
            if (*ptr == value) {
                remove_gap(i, 1);
                _size--; return true;
            } ++ptr;
        }
        return false;
    }


    E pop_front() { return pop(0); }
    E pop_back() { return pop(_size - 1); }
    E pop(int index) {
        range_check(index);
        E item = array[index];
        remove_gap(index, 1);
        _size--; return item;
    }


    Vector<E> sub_vector(int start, int end) {
        range_check(start); range_check(end);

        Vector<E> result(2*(end - start + 1));
        result._size = end - start + 1;

        E* result_it = result.array;
        E* this_it = array + start;

        for (int i = 0; i < result._size; ++i) {
            *result_it = *this_it;
            result_it++;
            this_it++;
        }
        return result;
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
};

#endif
