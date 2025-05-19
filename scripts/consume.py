# /// script
# requires-python = ">=3.11"
# dependencies = [
#   "pika",
# ]
# ///

import sys
import pika

def consume(topic, server="localhost"):
    connection = pika.BlockingConnection(pika.ConnectionParameters(server))
    channel = connection.channel()
    channel.exchange_declare(exchange=topic, exchange_type='topic', durable=True)

    # Create a random exclusive queue
    result = channel.queue_declare(queue='', exclusive=True)
    queue_name = result.method.queue
    print("Created queue:", queue_name)

    # Bind to the topic exchange with routing key pattern
    channel.queue_bind(exchange=topic, queue=queue_name, routing_key="")

    def callback(ch, method, properties, body):
        print("Received:", body.decode())

    channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)
    print('Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python consume.py <topic>")
        sys.exit(1)

    consume(sys.argv[1])
