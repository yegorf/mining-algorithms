package com.example.mining_algorithms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {
    List<Block> blockchain = new ArrayList<>();
    int prefix = 4;
    String prefixString = new String(new char[prefix]).replace('\0', '0');

    @org.junit.Test
    public void test() {
        Block newBlock = new Block(
                "The is a New Block.",
                "0000702dc1ee3965c82dc358c0b4f4719c8fd12e38fb3066dc9afa9297cb564f"/*blockchain.get(blockchain.size() - 1).getHash()*/,
                new Date().getTime());
        newBlock.mineBlock(prefix);
        assertEquals(newBlock.getHash().substring(0, prefix), prefixString);
        blockchain.add(newBlock);
    }

    @org.junit.Test
    public void testAlg() throws NoSuchAlgorithmException {
        String dataToHash = "kek looooooooooooooool";
        byte[] bytes = dataToHash.getBytes(StandardCharsets.UTF_8);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.digest(bytes);

        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        System.out.println("java - " + buffer.toString());
    }

    @org.junit.Test
    public void testGit() throws NoSuchAlgorithmException {
        String dataToHash = "kek";
        byte[] bytes = dataToHash.getBytes(StandardCharsets.UTF_8);

        byte[] hash = SHA256Encoder.hash(bytes);

        StringBuilder buffer = new StringBuilder();
        for (byte b : hash) {
            buffer.append(String.format("%02x", b));
        }
        System.out.println("git - " + buffer.toString());
    }
}
