#include <iostream>
#include <fstream>

using namespace std;

// Register a new user
void registerUser() {
    string username, password;
    cout << "Enter a new username: ";
    cin >> username;
    cout << "Enter a new password: ";
    cin >> password;

    ifstream readFile("users.txt");
    string u, p;
    while (readFile >> u >> p) {
        if (u == username) {
            cout << "Username already taken. Try another one.\n";
            return;
        }
    }
    readFile.close();

    ofstream writeFile("users.txt", ios::app);
    writeFile << username << " " << password << "\n";
    writeFile.close();

    cout << "Registration successful!\n";
}

// Login an existing user
void loginUser() {
    string username, password;
    cout << "Enter your username: ";
    cin >> username;
    cout << "Enter your password: ";
    cin >> password;

    ifstream readFile("users.txt");
    string u, p;
    bool success = false;
    while (readFile >> u >> p) {
        if (u == username && p == password) {
            success = true;
            break;
        }
    }
    readFile.close();

    if (success) {
        cout << "Login successful! Welcome, " << username << "!\n";
    } else {
        cout << "Login failed! Username or password incorrect.\n";
    }
}

int main() {
    int choice;

    while (true) {
        cout << "\n==== Login & Registration System ====\n";
        cout << "1. Register\n2. Login\n3. Exit\n";
        cout << "Choose option (1, 2 or 3): ";
        cin >> choice;

        if (choice == 1) {
            registerUser();
        } else if (choice == 2) {
            loginUser();
        } else if (choice == 3) {
            cout << "Exiting program. Goodbye!\n";
            break;
        } else {
            cout << "Invalid choice. Try again.\n";
        }
    }

    return 0;
}