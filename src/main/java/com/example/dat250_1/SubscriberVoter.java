package com.example.dat250_1;

import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.UnifiedJedis;

import java.util.Scanner;

public class SubscriberVoter {
    public static void main(String[] args) {
        UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");
        UnifiedJedis subscriber = new UnifiedJedis("redis://localhost:6379");
        Thread subscriberThread = new Thread(() -> {
            subscriber.psubscribe(new JedisPubSub() {
                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    System.out.println("Vote " + channel + ": " + message);
                }

                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    System.out.println("Subscribed to " + pattern);
                }
            }, "poll:*:vote");
        });
        subscriberThread.setDaemon(true);
        subscriberThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length == 4 && parts[0].equalsIgnoreCase("vote")) {

                    String pollId = parts[1];
                    String optionId = parts[2];
                    String userId = parts[3];

                    String channel = "poll:" + pollId + ":vote";
                    String message = optionId + ":" + userId;

                    jedis.publish(channel, message);
                    System.out.println("Vote " + channel + ": " + message);
            }
        }
        jedis.close();
        subscriber.close();
        scanner.close();
    }
}
