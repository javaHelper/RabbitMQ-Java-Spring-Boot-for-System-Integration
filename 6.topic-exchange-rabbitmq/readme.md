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

<img width="541" alt="Screenshot 2022-08-07 at 3 04 44 PM" src="https://user-images.githubusercontent.com/54174687/183284676-ce16dd4d-992f-44c6-986b-3ce8c9ced7dc.png">
<img width="619" alt="Screenshot 2022-08-07 at 3 04 56 PM" src="https://user-images.githubusercontent.com/54174687/183284682-b642931b-1707-44bb-acb4-5b23b1bec7d6.png">
<img width="525" alt="Screenshot 2022-08-07 at 3 05 11 PM" src="https://user-images.githubusercontent.com/54174687/183284683-10b8a90f-c426-4d20-b31c-a1902b08d52c.png">
<img width="869" alt="Screenshot 2022-08-07 at 3 05 22 PM" src="https://user-images.githubusercontent.com/54174687/183284686-3088c534-6f4c-4ccd-bba9-acbf7ef217be.png">

