# Employees Service

Employees Service that process Employees files and gather useful information from them.

Shows 3 main employees:
- managers which earn less than they should, and by how much
- managers which earn more than they should, and by how much
- employees which have a reporting line which is too long, and by how much
<br/>

Expected files structure:
```
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
```


## Pre-requirements

- installed Java 22 
- maven 

## Build

```bash
mvn clean install
```

## Run

With default file employeeFileFull.txt
```bash
mvn compile exec:java -DfilePath=%file_path%
```

With parametrized custom absolute path to Employees file:
```bash
mvn compile exec:java -DfilePath=%file_path%
```

## Assumptions and Tech Debt

- Decided to use lower & upper salary percentage limits and reporting line limit as Controller parameters since it simplifies testing and may be changed in the future
- Decided to use Mockito for testing since it makes isolation and really simplifies testing
- Used custom simple DI approach with several caveats:
  - the implementation does not support more than 1 implementation of the interface (and does not support interfaces to be honest)
  - does not support cyclic dependency
  - to support custom implementations of the classes used init(Map) method that recreates the config and may lead to several configs at the same time 
- The Application reads all file lines at the same time and stores them in the memory till flow finishes. Since only 1000 lines are used by the task it justified but big datafiles may lead to OOM

## Contributing

Please don't do anything, it is a test project. 

## License

[MIT](https://choosealicense.com/licenses/mit/)

## Author

- Maksym Koriak
- Maksym Koriak's Cat 
