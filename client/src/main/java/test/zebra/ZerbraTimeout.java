package test.zebra;

class ZerbraTimeout {
    private int responseCompletionTimeout;

    public ZerbraTimeout(int initialResponseTimeout, int responseCompletionTimeout, int maxTimeoutForRead) {
        this.responseCompletionTimeout = responseCompletionTimeout;
        this.initialResponseTimeout = initialResponseTimeout;
        this.maxTimeoutForRead = maxTimeoutForRead;
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Timeouts - ");
        stringBuilder.append("InitialResponseTimeout: ");
        stringBuilder.append(initialResponseTimeout);
        stringBuilder.append(" \\ ");
        stringBuilder.append("ResponseCompletionTimeout: ");
        stringBuilder.append(responseCompletionTimeout);
        stringBuilder.append(" \\ ");
        stringBuilder.append("MaxTimeoutForRead: ");
        stringBuilder.append(maxTimeoutForRead);

        return stringBuilder.toString();
    }

    public int getResponseCompletionTimeout() {
        return responseCompletionTimeout;
    }

    public int getInitialResponseTimeout() {
        return initialResponseTimeout;
    }

    public int getMaxTimeoutForRead() {
        return maxTimeoutForRead;
    }

    private int initialResponseTimeout;
    private int maxTimeoutForRead;

}
