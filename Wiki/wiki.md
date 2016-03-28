# JUnit Test Summary #
helenote/
    CreateNoteActivity.java
    DetailNoteActivity.java
    HistoryNoteActivity.java
    MainActivity.java
    NoteListAdapter.java
    NoteListFragment.java
    data/
        NotesContract.java
        NotesDbHelper.java
        NotesProvider.java
res/
    data/
    drawable/
    layout/
        activity_history_note.xml
        activity_main.xml
        activity_note_create.xml
        activity_note_detail.xml
        fragment_note_create.xml
        fragment_note_detail.xml
        fragment_note_list.xml
        note_item.xml
    menu/
        detail_menu.xml
        main_menu.xml
    mipmap-hdpi/
        ic_launcher.png
    mipmap-mdpi/
        ic_launcher.png
    mipmap-xhdpi/
        ic_launcher.png
    mipmap-xxhdpi/
        ic_launcher.png
    mipmap-xxxhdpi/
        ic_launcher.png
    values/
        colors.xml
        dimens.xml
        strings.xml
        styles.xml
    values-v21/
        styles.xml
    values-w820dp/
        dimens.xml

``` python
import os

def list_files(startpath):
    for root, dirs, files in os.walk(startpath):
        level = root.replace(startpath, '').count(os.sep)
        indent = ' ' * 4 * (level)
        print('{}{}/'.format(indent, os.path.basename(root)))
        subindent = ' ' * 4 * (level + 1)
        for f in files:
            print('{}{}'.format(subindent, f))
```

## Test Database ##

### Test Initialization ###

This is tested automatically by JUnit Test.

  - 03-27 20:57:15.227 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestDb)
  - 03-27 20:57:15.237 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestDb)
  - 03-27 20:57:15.237 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestDb)

### Test Create Database ###

This is to test whether database is correctly created. Including test tables' names and column names of each table. 

  - 03-27 20:57:15.237 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testCreateDb(com.example.spacegoing.helenote.data.TestDb)
  - 03-27 20:57:15.367 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testCreateDb(com.example.spacegoing.helenote.data.TestDb)
  - 03-27 20:57:15.367 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testCreateDb(com.example.spacegoing.helenote.data.TestDb)

### Test Note Table ###

This is to test whether each record in each table can be manipulated correctly. Including testing `insert` `update` `delete` methods.

  - 03-27 20:57:15.377 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testNoteTable(com.example.spacegoing.helenote.data.TestDb)
  - 03-27 20:57:15.587 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testNoteTable(com.example.spacegoing.helenote.data.TestDb)
  - 03-27 20:57:15.587 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testNoteTable(com.example.spacegoing.helenote.data.TestDb)

## Test Note Contract ##

### Test Initialization ###

This is tested automatically by JUnit Test.

  - 03-27 20:57:15.597 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestNoteContract)
  - 03-27 20:57:15.617 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestNoteContract)
  - 03-27 20:57:15.617 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestNoteContract)

### Test Build Note Uri ###

This is to test Note Contract's methods. Since it only contains `buildUri` methods. So only those methods are tested.

  - 03-27 20:57:15.627 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testBuildNoteUri(com.example.spacegoing.helenote.data.TestNoteContract)
  - 03-27 20:57:15.637 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testBuildNoteUri(com.example.spacegoing.helenote.data.TestNoteContract)
  - 03-27 20:57:15.637 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testBuildNoteUri(com.example.spacegoing.helenote.data.TestNoteContract)

## Test Note Provider ##

### Test Initialization ###

This is tested automatically by JUnit Test.

  - 03-27 20:57:15.647 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:15.727 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:15.727 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestProvider)
  
### Test Provider's Basic Query  ###

This is to test whether basic query of `Note Table` work as expected. Including testing `insert` and `query` methods.

  - 03-27 20:57:15.727 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testBasicNoteQuery(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:15.857 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testBasicNoteQuery(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:15.857 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testBasicNoteQuery(com.example.spacegoing.helenote.data.TestProvider)

### Test Provider's Basic Query  ###

This is to test whether basic query of `Revision Table` work as expected. Including testing `insert` and `query` methods.

  - 03-27 20:57:15.867 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testBasicRevisionQueries(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:16.057 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testBasicRevisionQueries(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:16.057 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testBasicRevisionQueries(com.example.spacegoing.helenote.data.TestProvider)

### Test Provider's GetType method ###

This is to test whether Provider's `GetType` return the correct `Type`.

  - 03-27 20:57:16.057 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testGetType(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:16.137 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testGetType(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:16.137 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testGetType(com.example.spacegoing.helenote.data.TestProvider)

### Test Provider is correctly registered ###

This is to test whether `Provider` is correctly registered in AndroidManifest file.

  - 03-27 20:57:16.137 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testProviderRegistry(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:16.197 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testProviderRegistry(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:16.197 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testProviderRegistry(com.example.spacegoing.helenote.data.TestProvider)

### Test Provider's update method ###

This is to test whether method `update` works properly. 

  - 03-27 20:57:16.197 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testUpdateNote(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:16.457 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testUpdateNote(com.example.spacegoing.helenote.data.TestProvider)
  - 03-27 20:57:16.457 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testUpdateNote(com.example.spacegoing.helenote.data.TestProvider)

## Test UriMatcher ##

### Test Initialization ###

This is tested automatically by JUnit Test.

  - 03-27 20:57:16.457 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestUriMatcher)
  - 03-27 20:57:16.457 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestUriMatcher)
  - 03-27 20:57:16.457 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testAndroidTestCaseSetupProperly(com.example.spacegoing.helenote.data.TestUriMatcher)

### Test UriMatcher ###

This is to test whether `UriMatcher` can return correct Type (int value).

  - 03-27 20:57:16.467 2713-2744/com.example.spacegoing.helenote I/TestRunner: started: testUriMatcher(com.example.spacegoing.helenote.data.TestUriMatcher)
  - 03-27 20:57:16.467 2713-2744/com.example.spacegoing.helenote I/TestRunner: finished: testUriMatcher(com.example.spacegoing.helenote.data.TestUriMatcher)
  - 03-27 20:57:16.467 2713-2744/com.example.spacegoing.helenote I/TestRunner: passed: testUriMatcher(com.example.spacegoing.helenote.data.TestUriMatcher)
