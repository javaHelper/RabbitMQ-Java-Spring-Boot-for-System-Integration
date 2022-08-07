# Topic Exchange
The logic behind the topic-exchange is similar to that of direct exchange. The only difference is here, there is no need to have an exact matching. The pattern used for routing can be in the form of Regular expression i.e. we can use symbols like dot(.), asterisk(*) or Hash(#).

- Multiple criteria routing
- Two special characters on routing key

# Coding example
- Create queues: q.picture.filter, q.picture.log
- Create exchange : x.picture2 ,type: topic

# Create bindings:
- q.picture.image, routing key: *.*.png
- q.picture.image, routing key: #.jpg
- q.picture.vector, routing key: *.*.svg
- q.picture.filter, routing key: mobile.#
- q.picture.log, routing key: *.large.svg