# Code refactoring to follow SOLID principles

## Single Responsibility Principle (SRP)

### Problem to consider:

* The `Ams` class has multiple responsibilities, such as sending commands, managing services, and handling
  configurations.
* The `SSHExecutorInterface` class also has multiple responsibilities, including managing SSH connections and executing
  commands.

### Solution:

* Refactor the `Ams` class to separate its responsibilities into different classes.
* Similarly, refactor the `SSHExecutorInterface` class to focus solely on managing SSH connections.

---

## Open/Closed Principle (OCP)

### Problem to consider:

* The `Ams` class is not easily extendable without modifying its code.

### Solution:

* Use an interfaces to allow for extension without modification.

---

## Liskov Substitution Principle (LSP)

### Problem to consider:

* Need to handle the new requirement for executing commands on Windows as well.
* Ensure that objects of a superclass can be replaced with objects of a subclass without affecting the correctness of
  the program.

### Solution:

* Create subclasses that override superclass methods in a way that does not break functionality. For example, ensure
  that any class implementing ` SshCommandExecutor` can be used interchangeably without altering the expected behavior.

---

## Interface Segregation Principle (ISP)

### Problem to consider:

* The `FileManager` class contains methods specific to Linux, making it difficult to extend or adapt for other operating
  systems, violating the Interface Segregation Principle.

### Solution:

* Refactor the `FileManger` class by defining a common interface for file operations and providing separate
  implementations for Linux and Windows, ensuring maintainability.