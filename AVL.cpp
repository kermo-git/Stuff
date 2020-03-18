#include <iostream>
#include <list>
#include <sstream>
#include <string>

using namespace std;

template <class K, class V>
class AVL {
    struct Node {
        K key;
        V value;
        int height = 1;
        Node* left = NULL;
        Node* right = NULL;

        Node(const K& k, const V& v) {
            key = k; value = v;
        }
        Node(const K& k) {
            key = k;
        }


        void update_height() {
            int l_height = (left == NULL)? 0 : left->height;
            int r_height = (right == NULL)? 0 : right->height;
            height = (l_height > r_height)? 1 + l_height : 1 + r_height;
        }
        int balance_factor() {
            int l_height = (left == NULL)? 0 : left->height;
            int r_height = (right == NULL)? 0 : right->height;
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
                else
                    return right_rotation();
            } else if (balancefactor == -2) {
                if (right->balance_factor() == 1)
                    return right_left_rotation();
                else
                    return left_rotation();
            }
            update_height();
            return this;
        }


        Node* get(const K& k) {
            if (k < key)
                return (left == NULL)? NULL : left->get(k);
            if (k > key)
                return (right == NULL)? NULL : right->get(k);
            return this;
        }


        Node* smallest() {
            Node* current = this;
            while (current->left != NULL)
                current = current->left;
            return current;
        }
        Node* largest() {
            Node* current = this;
            while (current->right != NULL)
                current = current->right;
            return current;
        }


        void print(ostream& os, Node* largest) {
            if (left != NULL)
                left->print(os, largest);
            if (this == largest) {
                os << key << "=" << value;
                return;
            }
            os << key << "=" << value << ", ";
            if (right != NULL)
                right->print(os, largest);
            return;
        }
    };


    Node* root = NULL;
    int _size = 0;


    void delete_rec(Node* node) {
        if (node == NULL)
            return;
        delete_rec(node->left);
        delete_rec(node->right);
        delete node;
    }


    Node* clone(Node* other) {
        if (other == NULL)
            return NULL;
        Node* result = new Node(other->key, other->value);
        result->height = other->height;
        result->left = clone(other->left);
        result->right = clone(other->right);
        return result;
    }


    Node* temp;
    Node* insert_key(Node* node, const K& key) {
        if (node == NULL) {
            _size++;
            return temp = new Node(key);
        }
        if (key < node->key)
            node->left = insert_key(node->left, key);
        else if (key > node->key)
            node->right = insert_key(node->right, key);
        else {
            return temp = node;
        }
        return node->balance();
    }


    Node* remove_key(Node* node, const K& key) {
        if (node == NULL)
            return NULL;
        if (key < node->key)
            node->left = remove_key(node->left, key);
        else if (key > node->key)
            node->right = remove_key(node->right, key);
        else {
            if (node->left == NULL) {
                _size--;
                Node* tmp = node->right;
                delete node; return tmp;
            }
            if (node->right == NULL) {
                _size--;
                Node* tmp = node->left;
                delete node; return tmp;
            } else {
                Node* next = node->right->smallest();
                node->key = next->key;
                node->value = next->value;
                node->right = remove_key(node->right, node->key);
            }
        }
        return node->balance();
    }


public:
    AVL() {}
    AVL(const AVL<K, V>& other) {
        root = clone(other.root);
        _size = other._size;
    }
    ~AVL() { clear(); }


    int size() { return _size; }
    bool empty() { return _size == 0; }
    V& at(const K& key) {
        if (root != NULL) {
            Node *node = root->get(key);
            if (node != NULL)
                return node->value;
        }
        throw runtime_error("No such key.");
    }
    V& operator[](const K& key) {
        root = insert_key(root, key);
        return temp->value;
    }
    void erase(const K& key) {
        root = remove_key(root, key);
    }
    bool contains_key(const K& key) {
        return root->get(key) != NULL;
    }
    void clear() {
        delete_rec(root);
        root = NULL; _size = 0;
    }


    friend ostream& operator<<(ostream& os, AVL& map) {
        os << "{";
        if (map.root != NULL)
            map.root->print(os, map.root->largest());
        os << "}";
        return os;
    }
};
