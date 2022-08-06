# RabbitMQ-Java-Spring-Boot-for-System-Integration

# RabbitMQ Exchange
Exchange distributes (and copy) message to queue(s) based on Routing key in message. Routing key is like address in message's envelope.
Binding is link between a message with a queue or some queues.

There are several types of exchange, each routes message differently.
- Fanout
- Direct
- Topic

# Exchange Type: Fanout
- Multiple queues for single message
- Broadcast to all Queues

# Coding example
- Create 2 queues: q.hr.accounting and q.hr.marketing
- Create an exchange: x.hr

# Create binding to those two queues

# Exchange Type: Direct
- Send to selective queues
- Based on routing key

# Message can be discarded
Coding example
Create queues: q.picture.image and q.picture.vector
Create an exchange: x.picture

# Create bindings:
x.picture, routing key = jpg => q.picture.image
x.picture, routing key = svg => q.picture.vector

# Exchange Type: Topic
Multiple criteria routing
Two special characters on routing key
