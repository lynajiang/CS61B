# Gitlet Design Document

**Name**:
Lyna Jiang

## Classes and Data Structures
Gitlet:
Here we implement the methods: init, add, commit

Merge:
Here we implement the merge command.

Main:
Depending on the command, it calls the correct classes/methods.

## Algorithms
Main(): The glue of the code. From here we have the guidelines of what the system should act
when given certain commands. Similar to capers lab, we must throw an error exeception if the given commands
are wrong/not accepted. For each given command, the main class must have the right structure in which
it calls objects to be serialized or deserialized.

Init - creates a new directory. A new folder

Add - there are other Strings(file name) behind this command. The add function rewrites
the file currently in the staging area. There is only one file in the staging area which is in the .gitlet
file. If the user commits, it must commit the most current add file and only that file.

commit - there must be other Strings (messages) behind this command. The commit function saves a snapshot
of tracked file in the current commit. Each snapshot of files must be the same as its parent commit's snapshot.
Only keep versions of files exactly as they are, and not update them.
The rm command can untrack the current commit. After commit, the staging area must be clean.
The new commit is added as a new node in the commit tree as well as the date and time it was made.
Head pointer must point to the new commit

rm - there must be other String (file to remove) behind this command. Do not remove the file unless
it is tracked in the current commit.

status - displays the branches currently. Using formatter to display this status. 
Show the branches, staged files, removed files, modifications not staged for commit AND untracked files.

global-log - Displays all the information about all commits ever made. The order does not matter.

log - Show the commit id, the date and commit message. There is the "==="
before each commit. Use the java.util.formatter to format all the messages/commits.

checkout - take the version of file that is wanted. 
1. checkout -- [file name] new version of file is not staged. Takes the version of file as it
   exists in the head commit
2. checkout [commit id] -- [file name]
3. checkout [branch name]

branch - Creates a new branch with the given name. Throw an exception if the branch already exists.
When creating this branch, the pointer does not point at the new branch yet. Another command is needed.

rm- branch - delete the branch. Delete the pointer associated to the branch. Once the pointer is gone,
all commits to the branch is lost-- there is no need to purposefully delete all commits.

reset - Checks out all the files tracked by the given commit. The staging area is cleared.



Check if there is a gitlet in the directory. It should abort if there is one,
in order to not overwrite.

Merge():
Check if merge is necessary if not throw an exception. If there is a conflict between merges, throw
an exception.


## Persistence
In order to persist the settings of the gitlet project, we will need to save the
state of files after each commit, add call to gitlet. To do this,
1. Write the Gitlet Hashmaps to disk. We serialize them into bytes by the writeObject method
from the Utils class.
   
2. Write all the inputs into the disk. We serialize these into a file within a folder/directory.
This can also be done by the writeObject method from the Utils class. We need to make sure
   that the class implements the Serializable interface.
   
In order to retrieve our state, before executing any code, we need to search the saved files in the working
directory (we need to make sure it exists) and load the objects that we saved in them. After, we can
use readObject method from Utils class to read the data of files and deserialize the objects we previously saved.