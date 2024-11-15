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