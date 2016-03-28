<!-- markdown-toc start - Don't edit this section. Run M-x markdown-toc-generate-toc again -->
**Table of Contents**

- [Helenote: Android Note Taker](#helenote-android-note-taker)
    - [Implemented Extensions](#implemented-extensions)
    - [References](#references)
    - [UML Graph for Helenote](#uml-graph-for-helenote)
    - [Relationship between Activities](#relationship-between-activities)
- [Introduction](#introduction)
    - [First Step: Create a note](#first-step-create-a-note)
    - [Second Step: Filter notes by label](#second-step-filter-notes-by-label)
    - [Third Step: Search notes by Key Words](#third-step-search-notes-by-key-words)
    - [Fourth Step: Edit notes](#fourth-step-edit-notes)
    - [Fifth Step: View Revision History](#fifth-step-view-revision-history)
    - [Sixth Step: Share your note](#sixth-step-share-your-note)
- [JUnit Test Summary](#junit-test-summary)
    - [Test Database](#test-database)
        - [Test Initialization](#test-initialization)
        - [Test Create Database](#test-create-database)
        - [Test Note Table](#test-note-table)
    - [Test Note Contract](#test-note-contract)
        - [Test Initialization](#test-initialization)
        - [Test Build Note Uri](#test-build-note-uri)
    - [Test Note Provider](#test-note-provider)
        - [Test Initialization](#test-initialization)
        - [Test Provider's Basic Query](#test-providers-basic-query)
        - [Test Provider's Basic Query](#test-providers-basic-query)
        - [Test Provider's GetType method](#test-providers-gettype-method)
        - [Test Provider is correctly registered](#test-provider-is-correctly-registered)
        - [Test Provider's update method](#test-providers-update-method)
    - [Test UriMatcher](#test-urimatcher)
        - [Test Initialization](#test-initialization)
        - [Test UriMatcher](#test-urimatcher)
- [Directory Structure](#directory-structure)

<!-- markdown-toc end -->
# Helenote: Android Note Taker
Helenote is a note taker for Android Platform. This is a project for
COMP6442 Assignment 1.

## Implemented Extensions

- Adapt to a variety of different devices. Including landscape and tablets.

- Notes can be filtered by `label`

- Search View

- Share using other APPs

- Revision History

- Undo edit

## References
This project referenced many source files (especially JUnit Test Module) and templates from [Udacity Sunshine] (https://github.com/udacity/Sunshine-Version-2). Udacity Sunshine is a project of a free online Android Course by Google. The source files are free to use.

All source files referenced are introduced to this project with significant modifications.

## UML Graph for Helenote

The following UML graph are basic classes to implement the program.

![Helenote](/uploads/a2470d24c750f6e80a37f44d40801b41/Helenote.png)

## Relationship between Activities

![Screenshot_2016-03-29_00.14.36](/uploads/4dd505cd16229d4690978e42a0c30a41/Screenshot_2016-03-29_00.14.36.png)

# Introduction

The *Helenote* APP is an Android plain-text note taker. You can create, save, modify and delete notes. You can even label each note and search for key words. To help you keep record, you can view history version of your note. After you finished, you can share your notes through your favorite social network APPs. 

## First Step: Create a note
![0](/uploads/a1cb6ca6a3e86a6f07250353487c12f4/0.png)

This is the main panel. In this panel you can create note. You can also view other notes by clicking on the item. We will introduce this later. By now just click on the `create` button and you will be guided to the following panel:

![1-create](/uploads/d9a254c7a5f1ce0ab0be6caf61430a9b/1-create.png)

This is where you can input your note. You don't need to save your note manually. The content you input is saved automatically in the background. You can undo changes if you want. After you finish editing, you can simply click on the `<-` back arrow to back to the main panel.

![2](/uploads/957c3e7b4a85464cb960048396fd6346/2.png)

After you created a note, the note is automatically populated in the main panel.

## Second Step: Filter notes by label

On the main panel, you can enter the main menu by click on the upper right conner. 

![3-mainMenu](/uploads/fc49fe81d13a5b4426e3e6440918e956/3-mainMenu.png)

There are two functions. You can either filter notes by label or search key words. We first introduce how to filter notes by label. Click on `label` button then you can enter the *label filter* panel. As you can see, in this panel you can input the label you want. Once you finish, you can click `filt` button. This will popup notes only belong to the label you input. See the following screenshot as an example. The first screenshot displays notes have "Red" label. The second displays "Sample" ones.

![4label](/uploads/cd133e0344850d770eed4204bfd3c424/4label.png)

![5](/uploads/e93d8d014bca7a772f67e39e184d584c/5.png)

## Third Step: Search notes by Key Words

Sometimes you may also need to search notes by key words. On main panel click `search` button. You will be guided to *search panel*. Similar to *filter panel*, you can input key words here then click on `search` button. For example, if you input `first` and click `search`. Then our first note which contains the word `first` will be popup on the main panel:

![6-search](/uploads/8d4724f20b50f4e0624efc08bb255515/6-search.png)

## Fourth Step: Edit notes

You can keep editing your notes any time you want by clicking the item on main panel. This will guide you to the `detail panel`. You can keep editing and undo changes if you made mistakes. 

![7-detail](/uploads/7e9f1fb4de3cd27461e9cf49a0d348fb/7-detail.png)

In `detail panel`, if you click on the upper right conner, you can enter the detail menu. There are two functions you can perform. You can either view the revision history of the note or share the note by choosing you favorite APPs. First we'll show you how to view revision history.

## Fifth Step: View Revision History

Clicking on `history` button, you will be guided into the *history panel*. On this time all history of this panel will be displayed. The order is sorted descendant. Namely from the newest note to oldest note. The upper line is the note you are viewing. The `Last edited time` is the last edited time of that version. You can view detail by clicking on the item.

![8-history](/uploads/d036d06e91c32fd42b45ae468ce02f49/8-history.png)

## Sixth Step: Share your note

The most exciting function is share your note. By clicking on the `share` button you can easily share your note by choosing your favorite app. If you click on `share` button, a list of Apps with *share* function will be displayed as following screenshot:

![9-share](/uploads/cf57685a07195caa528232f0c6b133cf/9-share.png)

You can choose any App you like. For example, in this case we choose * Message * App. Helenote will guide you to *Message* App. And the best thing is the content of your note are already input on the edit text line. You can directly send it to your contacts.

![10](/uploads/e75140a9343c57a42a1293a51d9bd59e/10.png)

# JUnit Test Summary #

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

# Directory Structure
- helenote/

-     CreateNoteActivity.java

-     DetailNoteActivity.java

-     HistoryNoteActivity.java

-     LabelNoteActivity.java

-     MainActivity.java

-     NoteListAdapter.java

-     NoteListFragment.java

-     SearchNoteActivity.java

-     data/

-         NotesContract.java

-         NotesDbHelper.java

-         NotesProvider.java

-         CreateNoteActivity.java

- res/

-     layout/

-         activity_history_note.xml

-         activity_main.xml

-         activity_note_create.xml

-         activity_note_detail.xml

-         activity_search_note.xml

-         activity_set_label.xml

-         fragment_note_create.xml

-         fragment_note_detail.xml

-         fragment_note_list.xml

-         note_item.xml

-     menu/

-         detail_menu.xml

-         main_menu.xml
