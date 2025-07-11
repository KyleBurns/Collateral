# Collateral

A service which calculates collateral value (to 2 decimal places) for given accounts, which hold certain positions.

The API can be accessed through a POST request to `/api/collateral`, with a request body containing a list of account ID strings in the following format:

```
{
  "accountIds": ["E1", "E2"]
}
```

### Assumptions Made

- Assets can have a fractional quantity (e.g. owning fractional shares), so are represented by `double` values.

- Calls to external services to retrieve required data are done through POST requests to endpoints defined in application.properties.

- An account/asset pair has a non-null discount factor if and only if it is deemed to be eligible.

- An account/asset pair being considered ineligible is functionally equivalent to having a discount factor value of zero.

- A quantity or price value can never be negative, and the service should throw an exception if detected.
