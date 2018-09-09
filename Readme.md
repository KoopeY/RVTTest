# RVT test
Simple rest services

* Create account (POST /account/{:number}/create)
```
number - account number
sum - account balance
```

* Deposit to account (POST /account/{:to}/deposit)
```
to - account number
sum - sum to deposit
```

* Withdraw from account (POST /account/{:from}/withdraw)
```
from - account number
sum - sum to withdraw
```

* Transfer between accounts (POST /account/{:from}/transfer/{:to})
```
from - withdraw from account number
to - deposit to account number
sum - sum to transfer
```

## Status codes:
```
200 OK - success operation
201 CREATED - account created
400 BAD REQUEST - operation was not permitted
404 NOT_FOUND - account number was not found
500 INTERNAL ERROR - service exceptions
```