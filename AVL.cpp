#include <iostream>
using namespace std;


template <class K, class V>
class AVLMap {
    struct Node {
        K key; V value;
        Node* left = nullptr;
        Node* right = nullptr;
        int height = 1;


        Node(const K& k) { key = k; }
        Node(const Node& other) {
            key = other.key;
            value = other.value;

            if (other.left)
                left = new Node(*(other.left));
            if (other.right)
                right = new Node(*(other.right));
        }
        ~Node() {
            if (left)
                delete left;
            if (right)
                delete right;
        }


        void update_height() {
            int l_height = (left)? left->height : 0;
            int r_height = (right)? right->height : 0;
            height = (l_height > r_height)? 1 + l_height : 1 + r_height;
        }
        int balance_factor() {
            int l_height = (left)? left->height : 0;
            int r_height = (right)? right->height : 0;
            return l_height - r_height;
        }


        /*
         *    x                           y
         *   / \    <== right rotation   / \
         *  A   y                       x   C
         *     / \  left rotation ==>  / \
         *    B   C                   A   B
         */
        Node* left_rotation() {
            Node* new_root = right;
            right = right->left;
            new_root->left = this;

            update_height();
            new_root->update_height();
            return new_root;
        }
        Node* right_rotation() {
            Node* new_root = left;
            left = left->right;
            new_root->right = this;

            update_height();
            new_root->update_height();
            return new_root;
        }
        Node* right_left_rotation() {
            right = right->right_rotation();
            return left_rotation();
        }
        Node* left_right_rotation() {
            left = left->left_rotation();
            return right_rotation();
        }


        Node* balance() {
            int balancefactor = balance_factor();
            if (balancefactor == 2) {
                if (left->balance_factor() == -1)
                    return left_right_rotation();
                return right_rotation();
            } else if (balancefactor == -2) {
                if (right->balance_factor() == 1)
                    return right_left_rotation();
                return left_rotation();
            }
            update_height();
            return this;
        }


        void print(ostream& os, Node* largest) {
            if (left)
                left->print(os, largest);
            if (this == largest) {
                os << key << "=" << value;
                return;
            }
            os << key << "=" << value << ", ";
            if (right)
                right->print(os, largest);
        }
    };


    Node* root = nullptr;
    int _size = 0;


    Node* search(Node* node, const K& key) {
        if (node) {
            if (key < node->key)
                return search(node->left, key);
            if (key > node->key)
                return search(node->right, key);
        }
        return node;
    }


    Node* temp;
    Node* find_or_add_key(Node* node, const K& key) {
        if (!node) {
            _size++;
            return temp = new Node(key);
        }
        if (key < node->key)
            node->left = find_or_add_key(node->left, key);
        else if (key > node->key)
            node->right = find_or_add_key(node->right, key);
        else {
            return temp = node;
        }
        return node->balance();
    }


    Node* remove_key(Node* node, const K& key) {
        if (!node)
            return nullptr;
        if (key < node->key)
            node->left = remove_key(node->left, key);
        else if (key > node->key)
            node->right = remove_key(node->right, key);
        else {
            if (!(node->left)) {
                _size--;
                Node* tmp = node->right;
                delete node; return tmp;
            }
            else if (!(node->right)) {
                _size--;
                Node* tmp = node->left;
                delete node; return tmp;
            } else {
                Node* next = node->right;
                while (next->left)
                    next = next->left;

                node->key = next->key;
                node->value = next->value;
                node->right = remove_key(node->right, node->key);
            }
        }
        return node->balance();
    }


public:
    AVLMap() {}
    AVLMap(const AVLMap<K, V>& other) {
        root = (other.root)? new Node(*(other.root)) : nullptr;
        _size = other._size;
    }
    ~AVLMap() {
        if(root)
            delete root;
    }


    int size() { return _size; }
    bool empty() { return _size == 0; }
    void clear() {
        if (root)
            delete root;
        root = nullptr;
        _size = 0;
    }


    V& at(const K& key) {
        Node *node = search(root, key);
        if (node)
            return node->value;
        throw runtime_error("No such key.");
    }
    V& operator[](const K& key) {
        root = find_or_add_key(root, key);
        return temp->value;
    }
    void erase(const K& key) {
        root = remove_key(root, key);
    }
    bool contains_key(const K& key) {
        return (bool) search(root, key);
    }


    friend ostream& operator<<(ostream& os, AVLMap& map) {
        os << "{";
        if (map.root) {
            AVLMap::Node *largest = map.root;
            while (largest->right)
                largest = largest->right;
            map.root->print(os, largest);
        }
        os << "}";
        return os;
    }
};
