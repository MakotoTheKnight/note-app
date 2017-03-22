# Note-taking App

## To Run:

Either execute:

`./gradlew bootRepackage && java -jar build/libs/note-app-0.0.1-SNAPSHOT.jar`

OR

`./gradlew bootRun`

to fire up the app.

To test the app, run `./gradlew test`.

## Limitations

 - The note database is in-memory.  Changes are not preserved between runs.
 - The tests have been marked with `@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)`, which requires that the entire application context be rebuilt after every run.  This makes the tests slower, but eliminates the possibility of tests modifying the state of other tests.