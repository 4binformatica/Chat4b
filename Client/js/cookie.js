function storeValue(key, value) {
    if (localStorage) {
        localStorage.setItem(key, value);
    } else {
        $.cookies.set(key, value);
    }
}

function getStoredValue(key) {
    if (localStorage) {
        return localStorage.getItem(key);
    } else {
        return $.cookies.get(key);
    }
}

function deleteStoredValue(key) {
    if (localStorage) {
        localStorage.removeItem(key);
    } else {
        $.cookies.del(key);
    }
}