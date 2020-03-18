#include <iostream>
using namespace std;

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


    void downheap(int k) {
        int l_child = (k << 1) + 1;
        int r_child = (k << 1) + 2;

        if (r_child < _size) {
            if (wrong_order(k, l_child) ||
                wrong_order(k, r_child)) {

                int next = choose_next(l_child, r_child);
                swap(k, next); downheap(next);
            }
        } else if (l_child < _size && wrong_order(k, l_child)) {
            swap(k, l_child); downheap(l_child);
        }
    }


    void upheap(int k) {
        int parent = (k - 1) >> 1;
        if (parent >= 0 && wrong_order(parent, k)) {
            swap(parent, k); upheap(parent);
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
            throw runtime_error("Empty heap");
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
        return (this->tree[left] < this->tree[right])? left : right;
    };
public:
    MinHeap() {}
    MinHeap(const MinHeap<E>& other) : Heap<E>(other) {}
};


template <class E>
class Stack {
    struct Node {
        E item;
        Node* next = NULL;

        Node(const E& new_item, Node* next_node) {
            item = new_item;
            next = next_node;
        }
    };
    void check() {
        if (_size == 0)
            throw runtime_error("Empty stack");
    }
    Node* head = NULL;
    int _size = 0;
public:
    Stack() {}
    Stack(const Stack& other) {
        Node* current = other.head;
        while (current != NULL) {
            push(current->item);
            current = current->next;
        }
    }
    ~Stack() { clear(); }
    void clear() {
        _size = 0; Node* tmp;
        while (head) {
            tmp = head;
            head = head->next;
            delete tmp;
        }
    }
    int size() { return _size; }
    bool empty() { return _size == 0; }
    void push(const E& item) {
        head = new Node(item, head);
        _size++;
    }
    E peek() {
        check();
        return head->item;
    }
    E pop() {
        check();
        E item = head->item;
        Node* tmp = head;
        head = head->next;
        delete tmp;
        _size--;
        return item;
    }
};


template <class E>
class Queue {
    struct Node {
        E item;
        Node* next = NULL;
        Node(const E& new_item) {
            item = new_item;
        }
    };
    void check() {
        if (_size == 0)
            throw runtime_error("Empty queue");
    }
    Node* head = NULL;
    Node* tail = NULL;
    int _size = 0;
public:
    Queue() {}
    Queue(const Queue& other) {
        Node* current = other.head;
        while (current != NULL) {
            push(current->item);
            current = current->next;
        }
    }
    ~Queue() { clear(); }
    void clear() {
        _size = 0; Node* tmp;
        while (head) {
            tmp = head;
            head = head->next;
            delete tmp;
        }
    }
    int size() { return _size; }
    bool empty() { return _size == 0; }
    void push(const E& item) {
        if (_size == 0) {
            head = new Node(item);
            tail = head;
        } else {
            tail->next = new Node(item);
            tail = tail->next;
        }
        _size++;
    }
    E peek() {
        check();
        return head->item;
    }
    E pop() {
        check();
        E item = head->item;
        Node* tmp = head;
        head = head->next;
        delete tmp;
        _size--;
        return item;
    }
};
