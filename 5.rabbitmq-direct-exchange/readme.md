# Exchange Type: Direct
> Here, the routing of the message takes place where the binding key is exactly matched with the binding Queue, the message will be sent to the queue which consist of the exact match. It is also known as one-to-one exchange.

- Send to selective queues
- Based on routing key
- Message can be discarded

# Coding example
- Create queues: q.picture.image and q.picture.vector
- Create an exchange: x.picture
- Create bindings:
     - x.picture, routing key = jpg => q.picture.image
     - x.picture, routing key = svg => q.picture.vector