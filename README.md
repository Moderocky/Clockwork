Clockwork
=====

### Opus #23

A logic, collections and data library.

## Features

### Clock List

A simple list type with some useful features. \
`ClockList`s are backed by another list, typically an `ArrayList`.

#### Known Type

Unlike regular lists, they have a fixed type defined at creation. This can be obtained with `list.getType()`.
This type aids in serialisation- all elements conform to it.

The list type is either provided through the constructors (`new ClockList<>(type)` or `new ClockList<>(type, list)`) or
through compiler magic via an array `new ClockList<>()`.

Knowing the type allows easier array creation `String[] = list.toArray()` and allows this list to be serialised in
type-specific array formats.

#### Random Element

Clock lists have a simple `list.getRandom()` element getter that can accept a random source.

#### Clone

Clock lists can be cloned, which creates a new list object and a new backing list, allowing elements to be removed from
the clone without affecting the original.

### Primitive List

Array-based list implementations for primitive types. \
These are designed to reduce unnecessary memory usage and increase performance
in cases where number wrapper types are unnecessary.

These have the traditional list implementations that give and take values using wrappers.
These also have raw methods that use the primitive type directly.

Some semantics deviate from the typical rules of lists - primitive lists (naturally) cannot hold `null` values.
Some operations are not supported.

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
