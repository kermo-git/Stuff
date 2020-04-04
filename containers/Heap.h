#ifndef HEAP_H
#define HEAP_H
#include <iostream>


template <class E>
class Heap {
protected:
    int _size = 0;
    int capacity = 10;
    E* tree = new E[capacity];


    void resize(int new_capacity) {
        capacity = new_capacity;
        E* new_tree = new E[capacity];
        for (int i = 0; i < _size; i++) {
            new_tree[i] = tree[i];
        }
        delete[] tree;
        tree = new_tree;
    }


    virtual bool wrong_order(int parent, int child)=0;
    virtual int choose_next(int left, int right)=0;


    void downheap(int parent) {
        int left = (parent << 1) + 1;
        int right = (parent << 1) + 2;

        if (right < _size) {
            if (wrong_order(parent, left) ||
                wrong_order(parent, right)) {

                int next = choose_next(left, right);
                swap(parent, next); downheap(next);
            }
        } else if (left < _size && wrong_order(parent, left)) {
            swap(parent, left); downheap(left);
        }
    }


    void upheap(int child) {
        int parent = (child - 1) >> 1;
        if (parent >= 0 && wrong_order(parent, child)) {
            swap(parent, child); upheap(parent);
        }
    }


    void swap(int index_1, int index_2) {
        E tmp = tree[index_1];
        tree[index_1] = tree[index_2];
        tree[index_2] = tmp;
    }


public:
    Heap() {}
    Heap(const Heap<E>& other) {
        _size = other._size;
        capacity = other.capacity;
        tree = new E[capacity];
        for (int i = 0; i < capacity; i++)
            tree[i] = other.tree[i];
    }
    ~Heap() { delete[] tree; }


    bool empty() { return _size == 0; }
    int size() { return _size; }

    
    E peek() { return tree[0]; }
    E pop() {
        if (_size == 0) {
            delete[] tree;
            throw std::runtime_error("Empty heap.");
        }

        if (_size <= capacity >> 2)
            resize(capacity >> 1);

        E root = tree[0];
        tree[0] = tree[_size - 1];
        _size--;
        downheap(0);
        return root;
    }
    void push(const E& item) {
        if (_size >= capacity)
            resize(capacity << 1);

        tree[_size] = item;
        _size++;
        upheap(_size - 1);
    }
};


template <class E>
class MaxHeap: public Heap<E> {
    bool wrong_order(int parent, int child) {
        return this->tree[parent] < this->tree[child];
    };
    int choose_next(int left, int right) {
        return (this->tree[left] > this->tree[right])? left : right;
    };
public:
    MaxHeap() {}
    MaxHeap(const MaxHeap<E>& other) : Heap<E>(other) {}
};


template <class E>
class MinHeap: public Heap<E> {
    bool wrong_order(int parent, int child) {
        return this->tree[parent] > this->tree[child];
    };
    int choose_next(int left, int right) {
        return (this->tree[left] < this->tree[right]) ? left : right;
    };
public:
    MinHeap() {}
    MinHeap(const MinHeap<E>& other) : Heap<E>(other) {}
};

#endif
