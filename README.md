[![Codacy Badge](https://api.codacy.com/project/badge/Grade/331a650534dc4387af9b30e74e2c7508)](https://www.codacy.com/app/matikz1110/word-counter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=HerbMat/word-counter&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/HerbMat/word-counter.svg?branch=master)](https://travis-ci.org/HerbMat/word-counter)

## Simple word counter for Solid Studio.

### It allows:

1. Adding new words for database.
2. Getting number of occurrences of adding the given word.

### Additional requirements:

1. Application data does not need to survive after restart JVM.
2. Tests are required.
3. Application has to be ready to use component of Spring Boot.
4. Project has to be based on gradle or maven.
5. It would be nice to be published on public repository.

### Run tests

```
gradle test
```

### Run application

```
gradle bootRun
```