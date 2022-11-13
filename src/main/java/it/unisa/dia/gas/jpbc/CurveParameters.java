package it.unisa.dia.gas.jpbc;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Represents the set of parameters describing an elliptic curve.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.0
 */
public interface CurveParameters extends Serializable {

    /**
     * Returns <tt>true</tt> if a mapping for the specified
     * key exists.
     *
     * @param key key whose presence is to be tested
     * @return <tt>true</tt> if a mapping for the specified key exists.
     * @since 1.2.0
     */
    boolean containsKey(String key);

    /**
     * Returns the value as a string to which the specified key is mapped.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as a string to which the specified key is mapped.
     * @throws IllegalArgumentException if the specified key does not exists.
     * @since 1.2.0
     */
    String getString(String key);

    /**
     * Returns the value as a string to which the specified key is mapped.
     * If the mapping does not exist the passed defaultValue is returned.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as a string to which the specified key is mapped.
     * If the mapping does not exist the passed defaultValue is returned.
     * @since 1.2.0
     */
    String getString(String key, String defaultValue);

    /**
     * Returns the value as an int to which the specified key is mapped.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as an int to which the specified key is mapped.
     * @throws IllegalArgumentException if the specified key does not exists.
     * @since 1.2.0
     */
    int getInt(String key);

    /**
     * Returns the value as an int to which the specified key is mapped.
     * If the mapping does not exist the passed defaultValue is returned.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as an int to which the specified key is mapped.
     * If the mapping does not exist the passed defaultValue is returned.
     * @since 1.2.0
     */
    int getInt(String key, int defaultValue);

    /**
     * Returns the value as a BigInteger to which the specified key is mapped.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as a BigInteger to which the specified key is mapped.
     * @throws IllegalArgumentException if the specified key does not exists.
     * @since 1.2.0
     */
    BigInteger getBigInteger(String key);

    /**
     * Returns the value as a BigInteger to which the specified key is mapped.
     * If the mapping does not exist the passed defaultValue is returned.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as a BigInteger to which the specified key is mapped.
     * If the mapping does not exist the passed defaultValue is returned.
     * @since 1.2.0
     */
    BigInteger getBigInteger(String key, BigInteger defaultValue);

    /**
     * Returns the value as a long to which the specified key is mapped.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as a long to which the specified key is mapped.
     * @throws IllegalArgumentException if the specified key does not exists.
     * @since 1.2.0
     */
    long getLong(String key);

    /**
     * Returns the value as a long to which the specified key is mapped.
     * If the mapping does not exist the passed defaultValue is returned.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as a long to which the specified key is mapped.
     * If the mapping does not exist the passed defaultValue is returned.
     * @since 1.2.0
     */
    long getLong(String key, long defaultValue);

    /**
     * Returns the value as an array of bytes to which the specified key is mapped.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as an array of bytes to which the specified key is mapped.
     * @throws IllegalArgumentException if the specified key does not exists.
     * @since 1.2.0
     */
    byte[] getBytes(String key);

    /**
     * Returns the value as an array of bytes to which the specified key is mapped.
     *
     * @param key the key whose associated value is to be returned
     * @return the value as an array of bytes to which the specified key is mapped.
     * @throws IllegalArgumentException if the specified key does not exists.
     * @since 1.2.1
     */
    byte[] getBytes(String key, byte[] defaultValue);

    /**
     * Returns a string representation using the passed separator
     * between the values.
     * @param separator the separator to be used between the values.
     * @return a string representation.
     * @since 1.2.0
     */
    String toString(String separator);
}
