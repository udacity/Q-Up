# Q-Up
Toy app for SQLite lesson in the Android ND.

The app helps organize waiting lists for restaurants etc.
It allows the user to add names of guests to a queue and the number of the party, then when a table becomes available for a guest in the queue to be seated, they can be swiped away to be removed off the queue.

# Structure
The app contains 1 activity and the layout includes input fields for name and party count and a button below to add the guest to a recyclerview underneath the button.
The data is stored in a local SQLite DB and the recyclerview is updated on every change.
