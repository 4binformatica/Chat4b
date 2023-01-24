/**
 * If localStorage is available, use it, otherwise use cookies
 * 
 * @param key The key to store the value under.
 * @param value The value to store.
 */
function storeValue(key, value) {
    if (localStorage) {
        localStorage.setItem(key, value);
    } else {
        $.cookies.set(key, value);
    }
}

/**
 * If localStorage is available, use it, otherwise use cookies
 * 
 * @param key The key to store the value under.
 * @return The value of the key.
 */
function getStoredValue(key) {
    if (localStorage) {
        return localStorage.getItem(key);
    } else {
        return $.cookies.get(key);
    }
}

/**
 * If localStorage is available, use it to delete the key, otherwise use the jQuery cookie plugin to
 * delete the key.
 * 
 * @param key The key to store the value under.
 */
function deleteStoredValue(key) {
    if (localStorage) {
        localStorage.removeItem(key);
    } else {
        $.cookies.del(key);
    }
}