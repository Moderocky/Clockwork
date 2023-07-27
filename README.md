Clockwork
=====

### Opus #23

A logic, collections and data library.

## Features

### Table

A fixed-size two-dimensional grid of typed entries. \
Elements are stored according to `(x, y)` coordinates in columns and rows.

`Table`s also function as a collection, so the entire element set can be iterated over.

#### Making a Table

A table can be created with a type, width and length.

```java
final Table<String> table=new Table<>(String.class,3,2);
```

This will create a `3` by `2` table. All entries will start as `null`.

```
0 2 4
1 3 5
```

You can also create a table with an initial array of elements, which are entered by index number.

```java
final Table<String> table=new Table<>(3,2,"A","B","C");
```

This will create a `3` by `2` table and enter the initial data.
If the data set is smaller than the table capacity remaining slots will be `null`.
If the data set exceeds the table capacity then remaining entries will be ignored.

```
A C -
B - -
```

#### Accessing Data

There are **three** ways to directly access data from a table.

An element can be accessed via its index in the entire table. This indexing reads vertically, one column at a time.

```java
table.set(2,"hello");
```

```java
table.get(14);
```

An element can be accessed by its grid coordinates. This is the fastest access method. \
N.B. we go horizontally then vertically.

```java
table.get(20,35);
```

```java
table.set(22,41,"hello");
```

A row or column can be accessed as a unit, and then individual elements can be obtained by index in it.

```java
table.row(0).get(1);
```

```java
table.column(3).set(4,"hello");
```

Row and column iterators reflect changes on the table itself.

### Fixed Array List

A list implementation with a constant backing array and a fixed capacity.

#### Capacity

Fixed array lists can hold a set number of elements, shown by `list.capacity()`. \
If a list is full, it can be checked with `list.isFull()`.

#### Creation

Fixed array lists can either be given an array `new FixedArrayList<>(array)` which specifies its size and initial
elements,
or can be given both separately `new FixedArrayList<>(5, ...)`.

In either case, the elements will be shuffled into a continuous order and the pointer will be marked at the end.
This means the list does **not** support `null` elements during creation:
it would be impossible to tell whether the user supplied an empty array to mark the size, or an array full of genuine,
expected `null` values.

The list supports `null` elements in subsequent additions.

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

### Magic Map

**Note:** this requires Foundation 2 as a dependency.

An exceedingly fast `String -> Object` map. \
This implementation trades map creation time and flexibility for the fastest possible set/get access.

A magic map has a fixed set of keys, defined when the map is created during runtime.
A new map class is created with space allocated for each key.

> #### Example
>
> A magic map with the keys `foo` and `bar` would create something equivalent to:
> ```java 
> class ? extends MagicMap {
>     Object foo, bar;
> }
> ```
> The fields can be accessed directly by name if you are interacting with this from another meta-compiler task.

In order to provide the required map behaviour, the `get` and `put` methods implement a fast lookup switch to access the
field.

#### Access Helpers

An access helper interface can be provided for even faster access to an element, bypassing the lookup table.

> #### Example
> ```java 
> import mx.kenzie.clockwork.collection.MagicMap;
>
> interface MyMap extends MagicMap.Accessor {
>
>     Object foo();
>
>     String bar();
>
> }
> ```
> When a map is created with this accessor, the `foo` and `bar` methods will be implemented to get the fields, and cast
> their values where appropriate.
>
> A setter can be made with an `Object name(Object value)` method.

#### Extending a Map

One magic map can be made to extend another, by providing it as the super-type.

It will inherit all the keys (and extra behaviour) of its parent type.
