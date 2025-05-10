# 🃏 Blackjack Game

A simple GUI-based Blackjack game built with Java Swing. This project demonstrates Java OOP fundamentals, basic game logic, and event-driven programming using a clean, beginner-friendly structure.

---

## 📌 About

This project uses the Java programming language and applies key Object-Oriented Programming (OOP) principles. The game lets a single player go head-to-head with a virtual dealer. It includes core gameplay features like:

- Drawing cards via **Hit**
- Ending a turn with **Stand**
- Starting a new round with **Play Again**

The GUI was built using **Java Swing**, showcasing the use of layout managers, action listeners, and visual components.

---

## ⚠️ Note

This project utilized external references and tools during development, including:

- Youtube Tutorials(Mainly)
- StackOverflow  
- GeeksForGeeks  
- Oracle Java Documentation  
- AI-based coding assistance

Some logic and UI structuring are also inspired by past projects and personal experimentation.

---

## 🗂️ File Structure

The project is organized into multiple Java classes that keep logic modular and readable:
BlackJack/
│
├── Card.java # Handles individual card properties (suit, rank, value)
├── Deck.java # Manages a deck of 52 cards and shuffling logic
├── Hand.java # Represents the player's or dealer's hand
├── GameLogic.java # Contains win/lose condition checks and total calculations
├── GameGUI.java # The Swing interface with all buttons, visuals, and labels
└── Main.java # Entry point that safely launches the GUI


> `SwingUtilities.invokeLater()` is used to ensure all GUI actions are run on the **Event Dispatch Thread (EDT)**. Swing isn’t thread-safe, and using other threads can result in freezes or crashes.

**TL;DR / ELI5:** Think of the EDT like a one-way road. `invokeLater()` is the traffic officer managing the flow of UI updates so they don't crash into each other.

---

## ✅ Features

- Playable Blackjack mechanics
- Dealer draws until soft 17+
- Basic GUI with clear layout
- Score display for win/loss/tie
- Restart game with "Play Again" button

---

## ❌ Limitations

- No betting system or chip management  
- No multiplayer mode  
- No split, double down, or insurance  
- No persistent score tracking  

---

## 🛠️ How to Run

1. Clone the repository:
2. Open the folder in your Java IDE (IntelliJ, Eclipse, NetBeans, etc.)
3. Run Main.java

## 🚀 Possible Future Features
Add bet handling and virtual currency

Implement additional rules (double down, split)

Create settings panel with UI themes

Add sound effects and animations

## 📃 License
This project is open-source. Feel free to fork, modify, and build upon it for learning or enhancement purposes.

## 👨‍💻 Author
Made with 💻 and ☕ by Marc John Leonard M. Tongol
