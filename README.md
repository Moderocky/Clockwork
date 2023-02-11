Clockwork
=====

### Opus #23

A logic, collections and data library.

## Features

### I/O Queue

A single-entry task queue, designed for processing save/load tasks in sequence for environments where there is a risk of
concurrent access.

A `new IOQueue` can be obtained at any time, and should be stored in a field.

Data tasks can be queued using `queue.queue(task)`.
This will return a reference to the task, which can be used to `await()` from the program's thread, if the result is
required immediately.

When the queue is no longer required, it can be closed using the `shutdown(time)` method, which will try to process all
remaining tasks and report any that fail to complete within the allotted time.

Tasks are protected from being queued twice, so it is advisable to keep a reference to data-specific save or load tasks
in order to make use of this protection.

A task is removed from the queue *before* its execution. This means that if a task needs to be re-queued during its
execution it can be.

An I/O queue uses a single task thread. This makes sure the same data store will never be accessed twice at the same
time. \
If you are accessing multiple data stores, it is advisable to create separate queues for each, since there is no risk of
concurrent modification between them.
