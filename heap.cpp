#include <iostream>

template <class E>
class PriorityQueue {
    E* tree;
    int size;
    int capacity;
    bool maxheap;


    void resize(int new_capacity) {
        capacity = new_capacity;
        E* new_tree = new E[capacity];
        for (int i = 0; i < size; ++i) {
            new_tree[i] = tree[i];
        }
        delete[] tree;
        tree = new_tree;
    }
    void grow() {
        if (size == capacity) resize(capacity << 1);
    }
    void shrink() {
        if (size <= capacity >> 2) resize(capacity >> 1);
    }


    void maxheap_downheap() {
        int parent = 0, l_child, r_child;
        while (true) {
            l_child = 2*parent + 1;
            r_child = 2*parent + 2;

            bool swap_right = r_child < size && tree[parent] < tree[r_child];
            bool swap_left = l_child < size && tree[parent] < tree[l_child];

            if (swap_left && swap_right) {
                if (tree[r_child] > tree[l_child]) {
                    swap(parent, r_child); parent = r_child;
                }
                else { swap(parent, l_child); parent = l_child; }
            }
            else if (swap_left) {
                swap(parent, l_child); parent = l_child;
            }
            else if (swap_right) {
                swap(parent, r_child); parent = r_child;
            }
            else break;
        }
    }


    void maxheap_upheap() {
        int child = size - 1;
        while (true) {
            int parent = (child - 1)/2;
            if (parent >= 0 && tree[parent] < tree[child]) {
                swap(child, parent); child = parent;
            } else break;
        }
    }


    void minheap_downheap() {
        int parent = 0;
        while (true) {
            int l_child = 2*parent + 1;
            int r_child = 2*parent + 1;

            bool swap_right = r_child < size && tree[parent] > tree[r_child];
            bool swap_left = l_child < size && tree[parent] > tree[l_child];

            if (swap_left && swap_right) {
                if (tree[r_child] < tree[l_child]) {
                    swap(parent, r_child); parent = r_child;
                }
                else { swap(parent, l_child); parent = l_child; }
            }
            else if (swap_left) {
                swap(parent, l_child); parent = l_child;
            }
            else if (swap_right) {
                swap(parent, r_child); parent = r_child;
            }
            else break;
        }
    }


    void minheap_upheap() {
        int child = size - 1;
        while (true) {
            int parent = (child - 1)/2;
            if (parent >= 0 && tree[parent] > tree[child]) {
                swap(child, parent); child = parent;
            } else break;
        }
    }


    void swap(int index_1, int index_2) {
        E tmp = tree[index_1];
        tree[index_1] = tree[index_2];
        tree[index_2] = tmp;
    }


public:
    PriorityQueue(bool is_maxheap) {
        maxheap = is_maxheap;
        capacity = 10;
        tree = new E[capacity];
    }
    ~PriorityQueue() { delete[] tree; }

    bool empty() { return size == 0;}

    E peek() { return tree[0]; }

    E pop() {
        if (size == 0)
            throw std::runtime_error("Empty heap");

        E root = tree[0];
        shrink();
        tree[0] = tree[size-1];
        size--;

        if (maxheap)
            maxheap_downheap();
        else
            minheap_downheap();

        return root;
    }


    void push(const E& item) {
        grow();
        tree[size] = item;
        size++;

        if (maxheap)
            maxheap_upheap();
        else
            minheap_upheap();
    }
};
