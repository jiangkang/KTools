/**
 * AlipayJSAPI
 * @author wangyou.ly
 * @version 3.1.0
 * @todo
 **/
;(function (self) {

  function PromisePolyfillImpl() {
    /*!
 * @overview es6-promise - a tiny implementation of Promises/A+.
 * @copyright Copyright (c) 2014 Yehuda Katz, Tom Dale, Stefan Penner and contributors (Conversion to ES6 API by Jake Archibald)
 * @license   Licensed under MIT license
 *            See https://raw.githubusercontent.com/stefanpenner/es6-promise/master/LICENSE
 * @version   4.1.0+f9a5575b
 */

(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
    typeof define === 'function' && define.amd ? define(factory) :
    (global.ES6Promise = factory());
}(this, (function () { 'use strict';

function objectOrFunction(x) {
  return typeof x === 'function' || typeof x === 'object' && x !== null;
}

function isFunction(x) {
  return typeof x === 'function';
}

var _isArray = undefined;
if (!Array.isArray) {
  _isArray = function (x) {
    return Object.prototype.toString.call(x) === '[object Array]';
  };
} else {
  _isArray = Array.isArray;
}

var isArray = _isArray;

var len = 0;
var vertxNext = undefined;
var customSchedulerFn = undefined;

var asap = function asap(callback, arg) {
  queue[len] = callback;
  queue[len + 1] = arg;
  len += 2;
  if (len === 2) {
    // If len is 2, that means that we need to schedule an async flush.
    // If additional callbacks are queued before the queue is flushed, they
    // will be processed by this flush that we are scheduling.
    if (customSchedulerFn) {
      customSchedulerFn(flush);
    } else {
      scheduleFlush();
    }
  }
};

function setScheduler(scheduleFn) {
  customSchedulerFn = scheduleFn;
}

function setAsap(asapFn) {
  asap = asapFn;
}

var browserWindow = typeof window !== 'undefined' ? window : undefined;
var browserGlobal = browserWindow || {};
var BrowserMutationObserver = browserGlobal.MutationObserver || browserGlobal.WebKitMutationObserver;
var isNode = typeof self === 'undefined' && typeof process !== 'undefined' && ({}).toString.call(process) === '[object process]';

// test for web worker but not in IE10
var isWorker = typeof Uint8ClampedArray !== 'undefined' && typeof importScripts !== 'undefined' && typeof MessageChannel !== 'undefined';

// node
function useNextTick() {
  // node version 0.10.x displays a deprecation warning when nextTick is used recursively
  // see https://github.com/cujojs/when/issues/410 for details
  return function () {
    return process.nextTick(flush);
  };
}

// vertx
function useVertxTimer() {
  if (typeof vertxNext !== 'undefined') {
    return function () {
      vertxNext(flush);
    };
  }

  return useSetTimeout();
}

function useMutationObserver() {
  var iterations = 0;
  var observer = new BrowserMutationObserver(flush);
  var node = document.createTextNode('');
  observer.observe(node, { characterData: true });

  return function () {
    node.data = iterations = ++iterations % 2;
  };
}

// web worker
function useMessageChannel() {
  var channel = new MessageChannel();
  channel.port1.onmessage = flush;
  return function () {
    return channel.port2.postMessage(0);
  };
}

function useSetTimeout() {
  // Store setTimeout reference so es6-promise will be unaffected by
  // other code modifying setTimeout (like sinon.useFakeTimers())
  var globalSetTimeout = setTimeout;
  return function () {
    return globalSetTimeout(flush, 1);
  };
}

var queue = new Array(1000);
function flush() {
  for (var i = 0; i < len; i += 2) {
    var callback = queue[i];
    var arg = queue[i + 1];

    callback(arg);

    queue[i] = undefined;
    queue[i + 1] = undefined;
  }

  len = 0;
}

function attemptVertx() {
  try {
    var r = require;
    var vertx = r('vertx');
    vertxNext = vertx.runOnLoop || vertx.runOnContext;
    return useVertxTimer();
  } catch (e) {
    return useSetTimeout();
  }
}

var scheduleFlush = undefined;
// Decide what async method to use to triggering processing of queued callbacks:
if (isNode) {
  scheduleFlush = useNextTick();
} else if (BrowserMutationObserver) {
  scheduleFlush = useMutationObserver();
} else if (isWorker) {
  scheduleFlush = useMessageChannel();
} else if (browserWindow === undefined && typeof require === 'function') {
  scheduleFlush = attemptVertx();
} else {
  scheduleFlush = useSetTimeout();
}

function then(onFulfillment, onRejection) {
  var _arguments = arguments;

  var parent = this;

  var child = new this.constructor(noop);

  if (child[PROMISE_ID] === undefined) {
    makePromise(child);
  }

  var _state = parent._state;

  if (_state) {
    (function () {
      var callback = _arguments[_state - 1];
      asap(function () {
        return invokeCallback(_state, child, callback, parent._result);
      });
    })();
  } else {
    subscribe(parent, child, onFulfillment, onRejection);
  }

  return child;
}

/**
  `Promise.resolve` returns a promise that will become resolved with the
  passed `value`. It is shorthand for the following:

  ```javascript
  let promise = new Promise(function(resolve, reject){
    resolve(1);
  });

  promise.then(function(value){
    // value === 1
  });
  ```

  Instead of writing the above, your code now simply becomes the following:

  ```javascript
  let promise = Promise.resolve(1);

  promise.then(function(value){
    // value === 1
  });
  ```

  @method resolve
  @static
  @param {Any} value value that the returned promise will be resolved with
  Useful for tooling.
  @return {Promise} a promise that will become fulfilled with the given
  `value`
*/
function resolve(object) {
  /*jshint validthis:true */
  var Constructor = this;

  if (object && typeof object === 'object' && object.constructor === Constructor) {
    return object;
  }

  var promise = new Constructor(noop);
  _resolve(promise, object);
  return promise;
}

var PROMISE_ID = Math.random().toString(36).substring(16);

function noop() {}

var PENDING = void 0;
var FULFILLED = 1;
var REJECTED = 2;

var GET_THEN_ERROR = new ErrorObject();

function selfFulfillment() {
  return new TypeError("You cannot resolve a promise with itself");
}

function cannotReturnOwn() {
  return new TypeError('A promises callback cannot return that same promise.');
}

function getThen(promise) {
  try {
    return promise.then;
  } catch (error) {
    GET_THEN_ERROR.error = error;
    return GET_THEN_ERROR;
  }
}

function tryThen(then, value, fulfillmentHandler, rejectionHandler) {
  try {
    then.call(value, fulfillmentHandler, rejectionHandler);
  } catch (e) {
    return e;
  }
}

function handleForeignThenable(promise, thenable, then) {
  asap(function (promise) {
    var sealed = false;
    var error = tryThen(then, thenable, function (value) {
      if (sealed) {
        return;
      }
      sealed = true;
      if (thenable !== value) {
        _resolve(promise, value);
      } else {
        fulfill(promise, value);
      }
    }, function (reason) {
      if (sealed) {
        return;
      }
      sealed = true;

      _reject(promise, reason);
    }, 'Settle: ' + (promise._label || ' unknown promise'));

    if (!sealed && error) {
      sealed = true;
      _reject(promise, error);
    }
  }, promise);
}

function handleOwnThenable(promise, thenable) {
  if (thenable._state === FULFILLED) {
    fulfill(promise, thenable._result);
  } else if (thenable._state === REJECTED) {
    _reject(promise, thenable._result);
  } else {
    subscribe(thenable, undefined, function (value) {
      return _resolve(promise, value);
    }, function (reason) {
      return _reject(promise, reason);
    });
  }
}

function handleMaybeThenable(promise, maybeThenable, then$) {
  if (maybeThenable.constructor === promise.constructor && then$ === then && maybeThenable.constructor.resolve === resolve) {
    handleOwnThenable(promise, maybeThenable);
  } else {
    if (then$ === GET_THEN_ERROR) {
      _reject(promise, GET_THEN_ERROR.error);
      GET_THEN_ERROR.error = null;
    } else if (then$ === undefined) {
      fulfill(promise, maybeThenable);
    } else if (isFunction(then$)) {
      handleForeignThenable(promise, maybeThenable, then$);
    } else {
      fulfill(promise, maybeThenable);
    }
  }
}

function _resolve(promise, value) {
  if (promise === value) {
    _reject(promise, selfFulfillment());
  } else if (objectOrFunction(value)) {
    handleMaybeThenable(promise, value, getThen(value));
  } else {
    fulfill(promise, value);
  }
}

function publishRejection(promise) {
  if (promise._onerror) {
    promise._onerror(promise._result);
  }

  publish(promise);
}

function fulfill(promise, value) {
  if (promise._state !== PENDING) {
    return;
  }

  promise._result = value;
  promise._state = FULFILLED;

  if (promise._subscribers.length !== 0) {
    asap(publish, promise);
  }
}

function _reject(promise, reason) {
  if (promise._state !== PENDING) {
    return;
  }
  promise._state = REJECTED;
  promise._result = reason;

  asap(publishRejection, promise);
}

function subscribe(parent, child, onFulfillment, onRejection) {
  var _subscribers = parent._subscribers;
  var length = _subscribers.length;

  parent._onerror = null;

  _subscribers[length] = child;
  _subscribers[length + FULFILLED] = onFulfillment;
  _subscribers[length + REJECTED] = onRejection;

  if (length === 0 && parent._state) {
    asap(publish, parent);
  }
}

function publish(promise) {
  var subscribers = promise._subscribers;
  var settled = promise._state;

  if (subscribers.length === 0) {
    return;
  }

  var child = undefined,
      callback = undefined,
      detail = promise._result;

  for (var i = 0; i < subscribers.length; i += 3) {
    child = subscribers[i];
    callback = subscribers[i + settled];

    if (child) {
      invokeCallback(settled, child, callback, detail);
    } else {
      callback(detail);
    }
  }

  promise._subscribers.length = 0;
}

function ErrorObject() {
  this.error = null;
}

var TRY_CATCH_ERROR = new ErrorObject();

function tryCatch(callback, detail) {
  try {
    return callback(detail);
  } catch (e) {
    TRY_CATCH_ERROR.error = e;
    return TRY_CATCH_ERROR;
  }
}

function invokeCallback(settled, promise, callback, detail) {
  var hasCallback = isFunction(callback),
      value = undefined,
      error = undefined,
      succeeded = undefined,
      failed = undefined;

  if (hasCallback) {
    value = tryCatch(callback, detail);

    if (value === TRY_CATCH_ERROR) {
      failed = true;
      error = value.error;
      value.error = null;
    } else {
      succeeded = true;
    }

    if (promise === value) {
      _reject(promise, cannotReturnOwn());
      return;
    }
  } else {
    value = detail;
    succeeded = true;
  }

  if (promise._state !== PENDING) {
    // noop
  } else if (hasCallback && succeeded) {
      _resolve(promise, value);
    } else if (failed) {
      _reject(promise, error);
    } else if (settled === FULFILLED) {
      fulfill(promise, value);
    } else if (settled === REJECTED) {
      _reject(promise, value);
    }
}

function initializePromise(promise, resolver) {
  try {
    resolver(function resolvePromise(value) {
      _resolve(promise, value);
    }, function rejectPromise(reason) {
      _reject(promise, reason);
    });
  } catch (e) {
    _reject(promise, e);
  }
}

var id = 0;
function nextId() {
  return id++;
}

function makePromise(promise) {
  promise[PROMISE_ID] = id++;
  promise._state = undefined;
  promise._result = undefined;
  promise._subscribers = [];
}

function Enumerator(Constructor, input) {
  this._instanceConstructor = Constructor;
  this.promise = new Constructor(noop);

  if (!this.promise[PROMISE_ID]) {
    makePromise(this.promise);
  }

  if (isArray(input)) {
    this._input = input;
    this.length = input.length;
    this._remaining = input.length;

    this._result = new Array(this.length);

    if (this.length === 0) {
      fulfill(this.promise, this._result);
    } else {
      this.length = this.length || 0;
      this._enumerate();
      if (this._remaining === 0) {
        fulfill(this.promise, this._result);
      }
    }
  } else {
    _reject(this.promise, validationError());
  }
}

function validationError() {
  return new Error('Array Methods must be provided an Array');
};

Enumerator.prototype._enumerate = function () {
  var length = this.length;
  var _input = this._input;

  for (var i = 0; this._state === PENDING && i < length; i++) {
    this._eachEntry(_input[i], i);
  }
};

Enumerator.prototype._eachEntry = function (entry, i) {
  var c = this._instanceConstructor;
  var resolve$ = c.resolve;

  if (resolve$ === resolve) {
    var _then = getThen(entry);

    if (_then === then && entry._state !== PENDING) {
      this._settledAt(entry._state, i, entry._result);
    } else if (typeof _then !== 'function') {
      this._remaining--;
      this._result[i] = entry;
    } else if (c === Promise) {
      var promise = new c(noop);
      handleMaybeThenable(promise, entry, _then);
      this._willSettleAt(promise, i);
    } else {
      this._willSettleAt(new c(function (resolve$) {
        return resolve$(entry);
      }), i);
    }
  } else {
    this._willSettleAt(resolve$(entry), i);
  }
};

Enumerator.prototype._settledAt = function (state, i, value) {
  var promise = this.promise;

  if (promise._state === PENDING) {
    this._remaining--;

    if (state === REJECTED) {
      _reject(promise, value);
    } else {
      this._result[i] = value;
    }
  }

  if (this._remaining === 0) {
    fulfill(promise, this._result);
  }
};

Enumerator.prototype._willSettleAt = function (promise, i) {
  var enumerator = this;

  subscribe(promise, undefined, function (value) {
    return enumerator._settledAt(FULFILLED, i, value);
  }, function (reason) {
    return enumerator._settledAt(REJECTED, i, reason);
  });
};

/**
  `Promise.all` accepts an array of promises, and returns a new promise which
  is fulfilled with an array of fulfillment values for the passed promises, or
  rejected with the reason of the first passed promise to be rejected. It casts all
  elements of the passed iterable to promises as it runs this algorithm.

  Example:

  ```javascript
  let promise1 = resolve(1);
  let promise2 = resolve(2);
  let promise3 = resolve(3);
  let promises = [ promise1, promise2, promise3 ];

  Promise.all(promises).then(function(array){
    // The array here would be [ 1, 2, 3 ];
  });
  ```

  If any of the `promises` given to `all` are rejected, the first promise
  that is rejected will be given as an argument to the returned promises's
  rejection handler. For example:

  Example:

  ```javascript
  let promise1 = resolve(1);
  let promise2 = reject(new Error("2"));
  let promise3 = reject(new Error("3"));
  let promises = [ promise1, promise2, promise3 ];

  Promise.all(promises).then(function(array){
    // Code here never runs because there are rejected promises!
  }, function(error) {
    // error.message === "2"
  });
  ```

  @method all
  @static
  @param {Array} entries array of promises
  @param {String} label optional string for labeling the promise.
  Useful for tooling.
  @return {Promise} promise that is fulfilled when all `promises` have been
  fulfilled, or rejected if any of them become rejected.
  @static
*/
function all(entries) {
  return new Enumerator(this, entries).promise;
}

/**
  `Promise.race` returns a new promise which is settled in the same way as the
  first passed promise to settle.

  Example:

  ```javascript
  let promise1 = new Promise(function(resolve, reject){
    setTimeout(function(){
      resolve('promise 1');
    }, 200);
  });

  let promise2 = new Promise(function(resolve, reject){
    setTimeout(function(){
      resolve('promise 2');
    }, 100);
  });

  Promise.race([promise1, promise2]).then(function(result){
    // result === 'promise 2' because it was resolved before promise1
    // was resolved.
  });
  ```

  `Promise.race` is deterministic in that only the state of the first
  settled promise matters. For example, even if other promises given to the
  `promises` array argument are resolved, but the first settled promise has
  become rejected before the other promises became fulfilled, the returned
  promise will become rejected:

  ```javascript
  let promise1 = new Promise(function(resolve, reject){
    setTimeout(function(){
      resolve('promise 1');
    }, 200);
  });

  let promise2 = new Promise(function(resolve, reject){
    setTimeout(function(){
      reject(new Error('promise 2'));
    }, 100);
  });

  Promise.race([promise1, promise2]).then(function(result){
    // Code here never runs
  }, function(reason){
    // reason.message === 'promise 2' because promise 2 became rejected before
    // promise 1 became fulfilled
  });
  ```

  An example real-world use case is implementing timeouts:

  ```javascript
  Promise.race([ajax('foo.json'), timeout(5000)])
  ```

  @method race
  @static
  @param {Array} promises array of promises to observe
  Useful for tooling.
  @return {Promise} a promise which settles in the same way as the first passed
  promise to settle.
*/
function race(entries) {
  /*jshint validthis:true */
  var Constructor = this;

  if (!isArray(entries)) {
    return new Constructor(function (_, reject) {
      return reject(new TypeError('You must pass an array to race.'));
    });
  } else {
    return new Constructor(function (resolve, reject) {
      var length = entries.length;
      for (var i = 0; i < length; i++) {
        Constructor.resolve(entries[i]).then(resolve, reject);
      }
    });
  }
}

/**
  `Promise.reject` returns a promise rejected with the passed `reason`.
  It is shorthand for the following:

  ```javascript
  let promise = new Promise(function(resolve, reject){
    reject(new Error('WHOOPS'));
  });

  promise.then(function(value){
    // Code here doesn't run because the promise is rejected!
  }, function(reason){
    // reason.message === 'WHOOPS'
  });
  ```

  Instead of writing the above, your code now simply becomes the following:

  ```javascript
  let promise = Promise.reject(new Error('WHOOPS'));

  promise.then(function(value){
    // Code here doesn't run because the promise is rejected!
  }, function(reason){
    // reason.message === 'WHOOPS'
  });
  ```

  @method reject
  @static
  @param {Any} reason value that the returned promise will be rejected with.
  Useful for tooling.
  @return {Promise} a promise rejected with the given `reason`.
*/
function reject(reason) {
  /*jshint validthis:true */
  var Constructor = this;
  var promise = new Constructor(noop);
  _reject(promise, reason);
  return promise;
}

function needsResolver() {
  throw new TypeError('You must pass a resolver function as the first argument to the promise constructor');
}

function needsNew() {
  throw new TypeError("Failed to construct 'Promise': Please use the 'new' operator, this object constructor cannot be called as a function.");
}

/**
  Promise objects represent the eventual result of an asynchronous operation. The
  primary way of interacting with a promise is through its `then` method, which
  registers callbacks to receive either a promise's eventual value or the reason
  why the promise cannot be fulfilled.

  Terminology
  -----------

  - `promise` is an object or function with a `then` method whose behavior conforms to this specification.
  - `thenable` is an object or function that defines a `then` method.
  - `value` is any legal JavaScript value (including undefined, a thenable, or a promise).
  - `exception` is a value that is thrown using the throw statement.
  - `reason` is a value that indicates why a promise was rejected.
  - `settled` the final resting state of a promise, fulfilled or rejected.

  A promise can be in one of three states: pending, fulfilled, or rejected.

  Promises that are fulfilled have a fulfillment value and are in the fulfilled
  state.  Promises that are rejected have a rejection reason and are in the
  rejected state.  A fulfillment value is never a thenable.

  Promises can also be said to *resolve* a value.  If this value is also a
  promise, then the original promise's settled state will match the value's
  settled state.  So a promise that *resolves* a promise that rejects will
  itself reject, and a promise that *resolves* a promise that fulfills will
  itself fulfill.


  Basic Usage:
  ------------

  ```js
  let promise = new Promise(function(resolve, reject) {
    // on success
    resolve(value);

    // on failure
    reject(reason);
  });

  promise.then(function(value) {
    // on fulfillment
  }, function(reason) {
    // on rejection
  });
  ```

  Advanced Usage:
  ---------------

  Promises shine when abstracting away asynchronous interactions such as
  `XMLHttpRequest`s.

  ```js
  function getJSON(url) {
    return new Promise(function(resolve, reject){
      let xhr = new XMLHttpRequest();

      xhr.open('GET', url);
      xhr.onreadystatechange = handler;
      xhr.responseType = 'json';
      xhr.setRequestHeader('Accept', 'application/json');
      xhr.send();

      function handler() {
        if (this.readyState === this.DONE) {
          if (this.status === 200) {
            resolve(this.response);
          } else {
            reject(new Error('getJSON: `' + url + '` failed with status: [' + this.status + ']'));
          }
        }
      };
    });
  }

  getJSON('/posts.json').then(function(json) {
    // on fulfillment
  }, function(reason) {
    // on rejection
  });
  ```

  Unlike callbacks, promises are great composable primitives.

  ```js
  Promise.all([
    getJSON('/posts'),
    getJSON('/comments')
  ]).then(function(values){
    values[0] // => postsJSON
    values[1] // => commentsJSON

    return values;
  });
  ```

  @class Promise
  @param {function} resolver
  Useful for tooling.
  @constructor
*/
function Promise(resolver) {
  this[PROMISE_ID] = nextId();
  this._result = this._state = undefined;
  this._subscribers = [];

  if (noop !== resolver) {
    typeof resolver !== 'function' && needsResolver();
    this instanceof Promise ? initializePromise(this, resolver) : needsNew();
  }
}

Promise.all = all;
Promise.race = race;
Promise.resolve = resolve;
Promise.reject = reject;
Promise._setScheduler = setScheduler;
Promise._setAsap = setAsap;
Promise._asap = asap;

Promise.prototype = {
  constructor: Promise,

  /**
    The primary way of interacting with a promise is through its `then` method,
    which registers callbacks to receive either a promise's eventual value or the
    reason why the promise cannot be fulfilled.

    ```js
    findUser().then(function(user){
      // user is available
    }, function(reason){
      // user is unavailable, and you are given the reason why
    });
    ```

    Chaining
    --------

    The return value of `then` is itself a promise.  This second, 'downstream'
    promise is resolved with the return value of the first promise's fulfillment
    or rejection handler, or rejected if the handler throws an exception.

    ```js
    findUser().then(function (user) {
      return user.name;
    }, function (reason) {
      return 'default name';
    }).then(function (userName) {
      // If `findUser` fulfilled, `userName` will be the user's name, otherwise it
      // will be `'default name'`
    });

    findUser().then(function (user) {
      throw new Error('Found user, but still unhappy');
    }, function (reason) {
      throw new Error('`findUser` rejected and we're unhappy');
    }).then(function (value) {
      // never reached
    }, function (reason) {
      // if `findUser` fulfilled, `reason` will be 'Found user, but still unhappy'.
      // If `findUser` rejected, `reason` will be '`findUser` rejected and we're unhappy'.
    });
    ```
    If the downstream promise does not specify a rejection handler, rejection reasons will be propagated further downstream.

    ```js
    findUser().then(function (user) {
      throw new PedagogicalException('Upstream error');
    }).then(function (value) {
      // never reached
    }).then(function (value) {
      // never reached
    }, function (reason) {
      // The `PedgagocialException` is propagated all the way down to here
    });
    ```

    Assimilation
    ------------

    Sometimes the value you want to propagate to a downstream promise can only be
    retrieved asynchronously. This can be achieved by returning a promise in the
    fulfillment or rejection handler. The downstream promise will then be pending
    until the returned promise is settled. This is called *assimilation*.

    ```js
    findUser().then(function (user) {
      return findCommentsByAuthor(user);
    }).then(function (comments) {
      // The user's comments are now available
    });
    ```

    If the assimliated promise rejects, then the downstream promise will also reject.

    ```js
    findUser().then(function (user) {
      return findCommentsByAuthor(user);
    }).then(function (comments) {
      // If `findCommentsByAuthor` fulfills, we'll have the value here
    }, function (reason) {
      // If `findCommentsByAuthor` rejects, we'll have the reason here
    });
    ```

    Simple Example
    --------------

    Synchronous Example

    ```javascript
    let result;

    try {
      result = findResult();
      // success
    } catch(reason) {
      // failure
    }
    ```

    Errback Example

    ```js
    findResult(function(result, err){
      if (err) {
        // failure
      } else {
        // success
      }
    });
    ```

    Promise Example;

    ```javascript
    findResult().then(function(result){
      // success
    }, function(reason){
      // failure
    });
    ```

    Advanced Example
    --------------

    Synchronous Example

    ```javascript
    let author, books;

    try {
      author = findAuthor();
      books  = findBooksByAuthor(author);
      // success
    } catch(reason) {
      // failure
    }
    ```

    Errback Example

    ```js

    function foundBooks(books) {

    }

    function failure(reason) {

    }

    findAuthor(function(author, err){
      if (err) {
        failure(err);
        // failure
      } else {
        try {
          findBoooksByAuthor(author, function(books, err) {
            if (err) {
              failure(err);
            } else {
              try {
                foundBooks(books);
              } catch(reason) {
                failure(reason);
              }
            }
          });
        } catch(error) {
          failure(err);
        }
        // success
      }
    });
    ```

    Promise Example;

    ```javascript
    findAuthor().
      then(findBooksByAuthor).
      then(function(books){
        // found books
    }).catch(function(reason){
      // something went wrong
    });
    ```

    @method then
    @param {Function} onFulfilled
    @param {Function} onRejected
    Useful for tooling.
    @return {Promise}
  */
  then: then,

  /**
    `catch` is simply sugar for `then(undefined, onRejection)` which makes it the same
    as the catch block of a try/catch statement.

    ```js
    function findAuthor(){
      throw new Error('couldn't find that author');
    }

    // synchronous
    try {
      findAuthor();
    } catch(reason) {
      // something went wrong
    }

    // async with promises
    findAuthor().catch(function(reason){
      // something went wrong
    });
    ```

    @method catch
    @param {Function} onRejection
    Useful for tooling.
    @return {Promise}
  */
  'catch': function _catch(onRejection) {
    return this.then(null, onRejection);
  }
};

function polyfill() {
    var local = undefined;

    if (typeof global !== 'undefined') {
        local = global;
    } else if (typeof self !== 'undefined') {
        local = self;
    } else {
        try {
            local = Function('return this')();
        } catch (e) {
            throw new Error('polyfill failed because global object is unavailable in this environment');
        }
    }

    var P = local.Promise;

    if (P) {
        var promiseToString = null;
        try {
            promiseToString = Object.prototype.toString.call(P.resolve());
        } catch (e) {
            // silently ignored
        }

        if (promiseToString === '[object Promise]' && !P.cast) {
            return;
        }
    }

    local.Promise = Promise;
}

// Strange compat..
Promise.polyfill = polyfill;
Promise.Promise = Promise;

Promise.polyfill();

return Promise;

})));

  }

  function shouldIgnorePolyfill(self) {
    var isSupport = false;
    var P = self.Promise;

    if (P) {
      var promise = null;
      var then = null;
      try {
        promise = P.resolve();
        then = promise.then;
      } catch (e) {
        // silently ignored
      }
      if (promise instanceof P && typeof then === 'function' && !P.cast) {
        isSupport = true;
      }
    }
    return isSupport;
  }

  if (!shouldIgnorePolyfill(self)) {
    PromisePolyfillImpl();
  }
})(self);
/**
 * AP SOURCE
 */
;(function (self) {
  'use strict';
  /********************* JSAPI functions ********************/
  // AlipayJSBridge

  var _JS_BRIDGE_NAME = 'AlipayJSBridge';
  var _JS_BRIDGE = self[_JS_BRIDGE_NAME];
  var _UA = navigator.userAgent || navigator.swuserAgent;
  var _MEDIA_BUSINESS = 'apm-h5';
  var _IS_SUPPORT_PROMISE;
  var window = self.window;
  var document = self.document;
  var console = self.console;
  var parseInt = self.parseInt;
  /**
   * å¾…æ‰§è¡Œé˜Ÿåˆ—ï¼Œå¤„ç† ready å‰çš„æŽ¥å£è°ƒç”¨
   */
  var _WAITING_QUEUE = [];

  //ç¼“å­˜
  var _CACHE = {
    getBAPSI: {
      isListening: false,
      lastState: 2,
      on: function on() {
        if (!_CACHE.getBAPSI.isListening) {
          _JS_BRIDGE.call('startMonitorBackgroundAudio');
          _CACHE.getBAPSI.isListening = true;
          AP.on('getBackgroundAudioPlayedStateInfo', _CACHE.getBAPSI.listener);
        }
      },
      off: function off() {
        AP.off('getBackgroundAudioPlayedStateInfo', _CACHE.getBAPSI.listener);
        _JS_BRIDGE.call('stopMonitorBackgroundAudio');
        _CACHE.getBAPSI.isListening = false;
      },
      listener: function listener(evt) {
        var data = evt.data || {};
        var state = data.status;
        var triggerEvent = ['backgroundAudioPause', 'backgroundAudioPlay', 'backgroundAudioStop'][state];
        if (triggerEvent && state !== _CACHE.getBAPSI.lastState) {
          AP.trigger(triggerEvent);
          _CACHE.getBAPSI.lastState = state;
        }
      }
    }
  };
  /**
   * JSAPI å¼‚æ­¥æŽ¥å£åˆ—è¡¨ï¼Œä¸‹é¢æ˜¯åˆ—è¡¨ä¸­å…·ä½“ä»£ç ç»“æž„çš„è¯´æ˜Ž
   * @type {Object}
   *
   * @String   m => mapping                   JSAPI åç§°æ˜ å°„ï¼Œå³å¯¹åº”çš„ AlipayJSBridge çš„æŽ¥å£åï¼Œæ–¹ä¾¿ç›´æŽ¥æ”¹å
   * @Object   e => extra                     JSAPI æ‰©å±•ä¿¡æ¯ï¼Œæ–¹ä¾¿è¿½åŠ è‡ªå®šä¹‰æ ‡è¯†
   *                                          handleResultSuccess: Boolean æ˜¯å¦å¤„ç† success å­—æ®µ
   *                                          handleEventData: Boolean æ˜¯å¦å¤„ç†äº‹ä»¶æºå¸¦çš„æ•°æ®ï¼Œå³è¿‡æ»¤æŽ‰ event å¯¹è±¡åªè¿”å›ž data
   *                                          optionModifier: Function å¯¹åŽŸæœ‰ option å…¥å‚åšè¿›ä¸€æ­¥å¤„ç†
   *
   * @Function b => before(opt, cb)           å‰ç½®å¤„ç†ï¼Œå¤„ç†å…¥å‚
   *           @param  {Object}     opt       åŽŸå§‹å…¥å‚ï¼Œå…¶ä¸­ opt._ æ˜¯è°ƒç”¨æŽ¥å£æ—¶å¯ç›´æŽ¥ä¼ å…¥çš„æŸä¸ªå‚æ•°
   *           @return {Object}               å¤„ç†è¿‡çš„å…¥å‚
   * @Function d => doing(_opt, cb, opt)      ä»£æ›¿æ‰§è¡Œï¼Œä»£æ›¿åŽŸæœ‰ api ç›´æŽ¥æ‰§è¡Œï¼Œä¼šå¿½ç•¥ AlipayJSBridge.call æŽ¥å£
      *        @param  {Object}    opt        åŽŸå§‹å…¥å‚
   *           @param  {Object}    _opt       before å¤„ç†è¿‡çš„å…¥å‚
   *           @param  {Function}   cb        æŽ¥å£å›žè°ƒå‡½æ•°ï¼Œå·²åœ¨ AP.call ä¸­å¤„ç†ï¼Œæ‰€ä»¥æ­¤å¤„ä¸€å®šæ˜¯ä¸€ä¸ª Function æ— éœ€åˆ¤æ–­
   * @Function a => after(res, _opt, opt)     åŽç½®å¤„ç†ï¼Œå¤„ç†å‡ºå‚ï¼Œå³æŽ¥å£è¿”å›žç»™å›žè°ƒå‡½æ•°çš„å€¼
   *           @param  {Object}     opt       åŽŸå§‹å…¥å‚
   *           @param  {Object}     _opt      ç» before å¤„ç†è¿‡çš„å…¥å‚
   *           @param  {Object}     res       JSAPI æŽ¥å£çš„åŽŸå§‹è¿”å›žå€¼
   *           @return {Object}               å¤„ç†è¿‡çš„æŽ¥å£è¿”å›žå€¼
   *
   */
  var _JSAPI = {
    /************************* alipayjsapi-inc å†…éƒ¨æŽ¥å£ï¼Œä¸‹ä¸ºå ä½ç¬¦ï¼Œå¤–éƒ¨å‘å¸ƒæ—¶ä¼šè¢«åˆ é™¤ *************************/


    /**
     * æ–°ç‰ˆè“ç‰™ç›¸å…³æŽ¥å£
     */
    openBluetoothAdapter: {},
    closeBluetoothAdapter: {},
    getBluetoothAdapterState: {},
    startBluetoothDevicesDiscovery: {
      b: function b(opt) {
        if (__isString(opt._)) {
          opt._ = [opt._];
        }
        _mapping(opt, {
          _: 'services'
        });
        return opt;
      }
    },
    stopBluetoothDevicesDiscovery: {},
    getBluetoothDevices: {
      b: function b(opt) {
        if (__isString(opt._)) {
          opt._ = [opt._];
        }
        _mapping(opt, {
          _: 'services'
        });
        return opt;
      },
      a: function a(res) {
        if (__isArray(res.devices)) {
          __forEach(res.devices, function (key, val) {
            _mapping(val, {
              manufacturerData: 'advertisData'
            });
          });
        }

        return res;
      }
    },
    getConnectedBluetoothDevices: {
      a: function a(res) {
        if (__isArray(res.devices)) {
          __forEach(res.devices, function (key, val) {
            _mapping(val, {
              manufacturerData: 'advertisData'
            });
          });
        }

        return res;
      }
    },
    connectBLEDevice: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'deviceId'
        });
        return opt;
      }
    },
    disconnectBLEDevice: {},
    writeBLECharacteristicValue: {},
    readBLECharacteristicValue: {},
    notifyBLECharacteristicValueChange: {},
    getBLEDeviceServices: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'deviceId'
        });
        return opt;
      }
    },
    getBLEDeviceCharacteristics: {},
    onBLECharacteristicValueChange: {
      //çœŸæ­£çš„äº‹ä»¶åï¼Œä¼šæŠŠé¦–å­—æ¯è‡ªåŠ¨è½¬æˆå°å†™ï¼Œå› æ­¤è¿™é‡Œä½¿ç”¨ map å¯é¿å…è¿™ä¸ªé—®é¢˜
      m: 'BLECharacteristicValueChange'

    },
    offBLECharacteristicValueChange: {
      m: 'BLECharacteristicValueChange'
    },
    onBluetoothAdapterStateChange: {},
    offBluetoothAdapterStateChange: {},
    onBLEConnectionStateChanged: {
      m: 'BLEConnectionStateChanged'

    },
    offBLEConnectionStateChanged: {
      m: 'BLEConnectionStateChanged'
    },
    onBluetoothDeviceFound: {
      a: function a(res) {
        return _mapping(res, {
          manufacturerData: 'advertisData'
        });
      }
    },
    offBluetoothDeviceFound: {},
    /**
     * end æ–°ç‰ˆè“ç‰™ç›¸å…³æŽ¥å£
     */

    pushBizWindow: {},
    compressImage: {
      b: function b(opt) {
        opt.level = __isUndefined(opt.level) ? 4 : opt.level;
        return _mapping(opt, {
          _: 'apFilePaths',
          level: 'compressLevel%d'
        });
      },
      d: function d(_opt, cb) {
        if (__isAndroid()) {
          _JS_BRIDGE.call('compressImage', _opt, cb);
        } else {
          _fakeCallBack(cb, {
            apFilePaths: _opt.apFilePaths || []
          });
        }
      }
    },

    /**
    * èŽ·å–å¯åŠ¨å‚æ•°ï¼Œå¹¶è®°å½•åœ¨ AP.launchParams
    * @method getLaunchParams
    * @param  {String}   null
    * @param  {Function} fn  å›žè°ƒ
    */
    getLaunchParams: {
      d: function d(opt, cb) {
        AP.launchParams = window.ALIPAYH5STARTUPPARAMS || _JS_BRIDGE.startupParams || {};
        if (__isFunction(cb)) {
          cb(AP.launchParams);
        }
      }
    },
    //æ—§ç‰ˆè“ç‰™æŽ¥å£ç§»é™¤

    onTabClick: {},
    offTabClick: {},
    onShare: {
      m: 'onShare'

    },
    offShare: {
      m: 'onShare'
    },
    connectSocket: {
      b: function b(opt) {
        return _mapping(opt, {
          headers: 'header'
        });
      }
    },
    sendSocketMessage: {
      b: function b(opt) {
        return _mapping(opt, {
          _: 'data'
        });
      }
    },
    closeSocket: {},
    onSocketOpen: {},
    offSocketOpen: {},
    onSocketMessage: {},
    offSocketMessage: {},
    onSocketError: {},
    offSocketError: {},
    onSocketClose: {},
    offSocketClose: {},

    ////////////////////////////// [AlipayJSAPI/ui] ////////////////////////////
    /**
     * æŽ¥å£å¯ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªå­—ç¬¦ä¸²ï¼ˆopt.contentï¼‰
     * ç»Ÿä¸€ alert å’Œ confirm çš„å†…å®¹å­—æ®µä¸º content
     */
    alert: {
      b: function b(opt) {
        opt = _mapping(opt, {
          _: 'content',
          content: 'message%s',
          buttonText: 'button%s'
        });
        if (!__isUndefined(opt.title)) {
          opt.title = _toType('%s', opt.title);
        }
        return opt;
      }
    },
    /**
     * æŽ¥å£å¯ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªå­—ç¬¦ä¸²ï¼ˆopt.contentï¼‰
     * ç»Ÿä¸€ alert å’Œ confirm çš„å†…å®¹å­—æ®µä¸º content
     */
    confirm: {
      b: function b(opt) {
        opt = _mapping(opt, {
          _: 'content%s',
          content: 'message%s',
          confirmButtonText: 'okButton%s',
          cancelButtonText: 'cancelButton%s'
        });
        if (!__isUndefined(opt.title)) {
          opt.title = _toType('%s', opt.title);
        }
        return opt;
      },
      a: function a(res) {
        return _mapping(res, {
          ok: 'confirm' //æ›´æ”¹ä¹‹å‰è¿”å›žå€¼é‡Œçš„ ok ä¸º confirm
        });
      }
    },
    /**
     * æŽ¥å£å¯ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªå­—ç¬¦ä¸²ï¼ˆopt.contentï¼‰
     */
    showToast: {
      m: 'toast',
      b: function b(opt) {
        //toast å†…å®¹å­—æ®µæœ¬æ¥å°±æ˜¯ content
        _mapping(opt, {
          _: 'content%s'
        });
        if (!__isString(opt.content)) {
          opt.content = _toType('%s', opt.content);
        }
        //opt.duration = opt.duration || 2000;
        return opt;
      }
    },
    hideToast: {},
    /**
     * æŽ¥å£å¯ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªå­—ç¬¦ä¸²ï¼ˆopt.contentï¼‰
     * æŽ¥å£æ”¹é€  opt.content => opt.text
     */
    showLoading: {
      b: function b(opt) {
        return _mapping(opt, {
          _: 'content', // %s æ²¡å¿…è¦åŠ ç»™ contentï¼Œ
          content: 'text%s' // å› ä¸ºæœ€åŽè°ƒç”¨æŽ¥å£æ—¶çœŸæ­£å…¥å‚æ˜¯ text
        });
      }
    },
    hideLoading: {},
    showNavigationBarLoading: {
      m: 'showTitleLoading'
    },
    hideNavigationBarLoading: {
      m: 'hideTitleLoading'
    },
    /**
     * æ•´åˆäº† setTitle, setTitleColor, setBarBottomLineColor ä¸‰ä¸ªæŽ¥å£
     * @type {Object}
     */
    setNavigationBar: {
      b: function b(opt) {
        // JSAPI åç§°å¤ªé•¿åˆå¤šæ¬¡å¼•ç”¨ï¼Œä¸åˆ©äºŽä»£ç åŽ‹ç¼©ï¼Œå›ºå•ç‹¬è®°å½•
        var st = 'setTitle';
        var stc = 'setTitleColor';
        var sblc = 'setBarBottomLineColor';
        var _opt = {};

        _opt[st] = {};
        _opt[stc] = {};
        _opt[sblc] = {};

        // æ˜ å°„ä¸åŒ JSAPI çš„å…¥å‚
        _opt[st] = _mapping(_opt[st], {
          _: 'title', //æŽ¥å£å¯ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªå­—ç¬¦ä¸²ï¼ˆopt.titleï¼‰
          title: 'title%s',
          image: 'image%b' //å¤„ç† image ä¸º base64 çš„æƒ…å†µï¼Œä¸º native ç§»é™¤æ ¼å¼å¤´
        }, opt);
        _opt[stc] = _mapping(_opt[stc], {
          backgroundColor: 'color%c',
          reset: 'reset'
        }, opt);
        _opt[sblc] = _mapping(_opt[sblc], {
          borderBottomColor: 'color%c'
        }, opt);

        return _opt;
      },
      d: function d(_opt, cb) {
        var st = 'setTitle';
        var stc = 'setTitleColor';
        var sblc = 'setBarBottomLineColor';
        var res = {};
        //setTitle
        if (!__isEmptyObject(_opt[st])) {
          _JS_BRIDGE.call(st, _opt[st]);
        }
        //setBarBottomLineColor
        if (!__isEmptyObject(_opt[sblc])) {
          _JS_BRIDGE.call(sblc, _opt[sblc]);
          if (__isNaN(_opt[sblc].color)) {
            res.error = 2;
            res.errorMessage = 'é¢œè‰²å€¼ä¸åˆæ³•';
          }
        }
        //setTitleColor
        if (!__isEmptyObject(_opt[stc])) {
          _JS_BRIDGE.call(stc, _opt[stc], function (result) {
            res = __extend(result, res);
            cb(res);
          });
        } else {
          //setTitle å’Œ setBarBottomLineColor æœ¬èº«æ²¡æœ‰å›žè°ƒ
          //ä¸ºä¿æŒæŽ¥å£ä¸€è‡´æ€§è¦æ¨¡æ‹Ÿä¸€ä¸ªå¼‚æ­¥å›žè°ƒ
          _fakeCallBack(cb, res);
        }
      }
    },
    showTabBar: {
      b: function b(opt) {
        //åˆ›å»º tabBar
        opt.action = 'create';
        //é»˜è®¤æ¿€æ´»ç¬¬ä¸€ä¸ª tab
        opt.activeIndex = opt.activeIndex || 0;
        //å…¶ä»–å±žæ€§æ˜ å°„
        _mapping(opt, {
          color: 'textColor%c',
          activeColor: 'selectedColor%c',
          activeIndex: 'selectedIndex%d'
        });

        if (__isArray(opt.items)) {
          var items = opt.items;
          //éœ€è¦å¤åˆ¶ä¸€ä»½ï¼Œä¸èƒ½åœ¨åŽŸæ•°ç»„ä¸Šä¿®æ”¹ï¼Œä¼šç ´åç”¨æˆ·æ•°æ®
          opt.items = [];
          items.forEach(function (item, i) {
            item = _mapping(__extend({}, item), {
              title: 'name%s',
              tag: 'tag%s',
              icon: 'icon%b',
              activeIcon: 'activeIcon%b',
              badge: 'redDot%s'
            }, {
              tag: i,
              // title: item.title,
              // icon: item.icon,
              // activeIcon: item.activeIcon,
              badge: __isUndefined(item.badge) ? '-1' : item.badge
            });
            item.icon = _toType('%b', item.icon);
            item.activeIcon = _toType('%b', item.activeIcon);
            opt.items.push(item);
          });
        }
        return opt;
      },
      d: function d(_opt, cb, opt) {
        var apiName = 'showTabBar';
        if (!__isUndefined(_CACHE.showTabBar)) {
          console.error(apiName + ' must be called at most once');
        } else {
          _CACHE.showTabBar = {
            opt: opt
          };
        }
        //ç›‘å¬ç‚¹å‡»äº‹ä»¶
        AP.on('tabClick', function (evt) {
          var res = {};
          _mapping(res, {
            tag: 'index%d'
          }, {
            tag: __isObject(evt.data) && evt.data.tag ? evt.data.tag : '0'
          });
          cb(res);
        });
        //è°ƒç”¨æ–¹æ³•
        _JS_BRIDGE.call('tabBar', _opt, function (result) {
          //result å¹¶éžçœŸæ­£çš„è¿”å›žå€¼ï¼Œä½†æ˜¯è¦å¤„ç†æŽ¥å£é”™è¯¯
          _handleApiError(apiName, result);
        });
      }
    },
    setTabBarBadge: {
      m: 'tabBar',
      b: function b(opt) {
        opt.action = 'redDot';
        _mapping(opt, {
          index: 'tag%s',
          badge: 'redDot%s'
        }, {
          index: opt.index
        });
        return opt;
      }
    },
    showActionSheet: {
      m: 'actionSheet',
      b: function b(opt) {
        _mapping(opt, {
          items: 'btns',
          cancelButtonText: 'cancelBtn%s'
        });
        //æŠŠæŒ‰é’®å­—æ®µè½¬æˆå­—ç¬¦ä¸²ï¼Œéžå­—ç¬¦ä¸²ä¼šå¯¼è‡´é’±åŒ…é—ªé€€
        if (__isArray(opt.btns)) {
          var btns = opt.btns;
          opt.btns = [];
          btns.forEach(function (item) {
            return opt.btns.push(item + '');
          });
        }
        //æŠŠå–æ¶ˆæŒ‰é’®å­—æ®µè½¬æˆå­—ç¬¦ä¸²ï¼Œéžå­—ç¬¦ä¸²ä¼šå¯¼è‡´ actionSheet å…¨å±
        if (__isUndefined(opt.cancelBtn)) {
          opt.cancelBtn = 'å–æ¶ˆ';
        }

        return opt;
      },
      a: function a(res, _opt) {
        if (__isArray(_opt.btns) && res.index === _opt.btns.length) {
          res.index = -1;
        }
        return res;
      }
    },
    redirectTo: {
      /**
       * å¢žåŠ  opt.data ä½œä¸º queryString æ‹¼åœ¨ url åŽé¢
       */
      b: function b(opt) {
        //ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªå­—ç¬¦ä¸²æ—¶å½“ä½œ opt.url å‚æ•°
        _mapping(opt, {
          _: 'url'
        });
        //å¦‚æžœæœ‰ data å‚æ•°åˆ™æž„é€ æœ‰ queryString çš„ url
        if (__isObject(opt.data)) {
          opt.url = __buildUrl(opt.url, opt.data);
        }
        return opt;
      },
      d: function d(_opt) {
        if (_opt.url) {
          window.location.replace(_opt.url);
        }
      }
    },
    pushWindow: {
      /**
       * å¢žåŠ  opt.data ä½œä¸º queryString æ‹¼åœ¨ url åŽé¢
       */
      b: function b(opt) {
        //ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªå­—ç¬¦ä¸²æ—¶å½“ä½œ opt.url å‚æ•°
        _mapping(opt, {
          _: 'url',
          params: 'param'
        });
        if (opt.url.indexOf('?') > -1) {
          console.warn('try opt.' + 'data' + ' instead of querystring');
        }
        if (opt.url.indexOf('__webview_options__') > -1) {
          console.warn('try opt.' + 'params' + ' instead of ' + '__webview_options__');
        }
        //å¦‚æžœæœ‰ data å‚æ•°åˆ™æž„é€ æœ‰ queryString çš„ url
        if (__isObject(opt.data)) {
          opt.url = __buildUrl(opt.url, opt.data);
          delete opt.data;
        }
        return opt;
      }
    },
    popWindow: {
      b: function b(opt) {
        opt = _fixOptData(opt);
        if (!__isObject(opt.data)) {
          opt.data = {
            ___forResume___: opt.data
          };
        }
        return opt;
      }
    },
    popTo: {
      /**
       * æŽ¥å£å¯ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªæ•°å­—ï¼ˆopt.indexï¼‰æˆ–è€…ä¸€ä¸ªå­—ç¬¦ä¸²ï¼ˆopt.urlPatternï¼‰
       */
      b: function b(opt) {
        _mapping(opt, {
          _: function () {
            var key = void 0;
            if (__isNumber(opt._)) {
              key = 'index';
            }
            if (__isString(opt._)) {
              key = 'urlPattern';
            }
            return key;
          }()
        });
        if (!__isObject(opt.data)) {
          opt.data = {
            ___forResume___: opt.data
          };
        }
        return opt;
      }
    },
    allowPullDownRefresh: {
      d: function d(opt) {
        var onPDR = 'onPullDownRefresh';
        _mapping(opt, {
          _: 'allow'
        });
        opt.allow = __isUndefined(opt.allow) ? true : !!opt.allow;

        if (__isObject(_CACHE[onPDR])) {
          _CACHE[onPDR].allow = opt.allow;
        } else {
          _CACHE[onPDR] = {
            allow: opt.allow
          };
          //ç›‘å¬äº‹ä»¶ï¼Œé€šè¿‡ event.preventDefault() é˜»æ­¢ä¸‹æ‹‰åˆ·æ–°
          //æ»¡è¶³ç”¨æˆ·åœ¨æ²¡æœ‰ç›‘å¬äº‹ä»¶çš„æƒ…å†µä¸‹è°ƒç”¨ AP.allowPullDownRefresh(false) ä»ç„¶ç”Ÿæ•ˆ
          AP.onPullDownRefresh();
        }
        if (_CACHE[onPDR].allow) {
          _JS_BRIDGE.call('restorePullToRefresh');
        } else {
          if (_CACHE[onPDR].event) {
            _CACHE[onPDR].event.preventDefault();
          }
        }
      }
    },

    choosePhoneContact: {
      m: 'contact'
    },
    /**
     * æœ€å¤šé€‰æ‹©10ä¸ªè”ç³»äººï¼Œåªéœ²å‡º count å‚æ•°ï¼Œå…¶ä»–å±è”½
     */
    chooseAlipayContact: {
      m: 'chooseContact',
      b: function b(opt) {
        var multi = 'multi';
        var single = 'single';
        _mapping(opt, {
          _: 'count'
        });
        if (__isUndefined(opt.count)) {
          opt.count = 1;
        }
        if (opt.count === 1) {
          opt.type = single;
        } else {
          opt.type = multi;
          if (opt.count <= 0 || opt.count > 10) {
            opt.multiMax = 10;
          } else {
            opt.multiMax = opt.count;
          }
        }
        delete opt.count;
        return opt;
      },
      a: function a(res) {
        if (__isArray(res.contacts)) {
          res.contacts.forEach(function (contact) {
            _mapping(contact, {
              headImageUrl: 'avatar',
              name: 'realName'
            });
            delete contact.from;
          });
        }
        return res;
      }
    },
    share: {
      b: function b(opt) {
        var startShareOpt = {};
        var shareToChannelOpt = {};
        startShareOpt.onlySelectChannel = ['ALPContact', 'ALPTimeLine', 'ALPCommunity', 'Weibo', 'DingTalkSession', 'SMS', 'Weixin', 'WeixinTimeLine', 'QQ', 'QQZone'];
        if (__hasOwnProperty(opt, 'bizType')) {
          startShareOpt.bizType = opt.bizType;
        }

        shareToChannelOpt = __extend({}, opt);
        delete shareToChannelOpt.bizType;
        delete shareToChannelOpt.onlySelectChannel;
        _mapping(shareToChannelOpt, {
          image: 'imageUrl'
        });

        _CACHE.share = {
          startShare: startShareOpt,
          shareToChannel: shareToChannelOpt
        };
        return opt;
      },
      d: function d(opt, cb) {
        //éšè—ç¬¬äºŒè¡Œ
        if (opt.showToolBar === false) {
          _JS_BRIDGE.call('setToolbarMenu', {
            menus: [],
            override: true
          });
        }
        //å”¤èµ·åˆ†äº«é¢æ¿
        _JS_BRIDGE.call('startShare', _CACHE.share.startShare, function (info) {
          var stcOpt = _CACHE.share.shareToChannel;
          if (info.channelName) {
            _JS_BRIDGE.call('shareToChannel', {
              name: info.channelName,
              param: stcOpt
            }, cb);
          } else {
            cb(info);
          }
        });
      }
    },
    datePicker: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'formate',
          formate: 'mode',
          currentDate: 'beginDate',
          startDate: 'minDate',
          endDate: 'maxDate'
        });
        switch (opt.mode) {
          case 'HH:mm:ss':
            opt.mode = 0;
            break;
          case 'yyyy-MM-dd':
            opt.mode = 1;
            break;
          case 'yyyy-MM-dd HH:mm:ss':
            opt.mode = 2;
            break;
          default:
            opt.mode = 1;
        }
        return opt;
      },
      a: function a(res) {
        if (__isString(res.date)) {
          //è¿”å›žæ ¼å¼ä¸ºyyyy-MM-dd
          res.date = res.date.replace(/\//g, '-').trim();
        }
        // if (res.error === 2 ) {
        //   const currentDate = _opt.currentDate || Date.now();
        //   const startDate = _opt.startDate;
        // }
        return res;
      }
    },
    chooseCity: {
      m: 'getCities',
      b: function b(opt) {
        var customCities;
        var customHotCities;
        _mapping(opt, {
          showHotCities: 'needHotCity',
          cities: 'customCities',
          hotCities: 'customHotCities'
        });
        //æ˜¾ç¤ºå®šä½åŸŽå¸‚
        if (opt.showLocatedCity === true) {
          opt.currentCity = '';
          opt.adcode = '';
        } else {
          delete opt.currentCity;
          delete opt.adcode;
        }
        delete opt.showLocatedCity;

        //è‡ªå®šä¹‰åŸŽå¸‚
        customCities = opt.customCities;
        if (!__isUndefined(opt.customCities)) {
          opt.customCities = mapArray(customCities);
        }
        //è‡ªå®šä¹‰çƒ­é—¨åŸŽå¸‚
        customHotCities = opt.customHotCities;
        if (!__isUndefined(opt.customHotCities)) {
          opt.customHotCities = mapArray(customHotCities);
        }

        function mapArray(arr) {
          var tempArr;
          if (__isArray(arr)) {
            tempArr = [];
            arr.forEach(function (city) {
              tempArr.push(_mapping({}, {
                city: 'name',
                adCode: 'adcode%s',
                spell: 'pinyin'
              }, city));
            });
            arr = tempArr;
          }
          return arr;
        }

        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          adcode: 'adCode'
        });
        return res;
      }
    },

    ////////////////////////////// äº‹ä»¶ /////////////////////////////////
    onBack: {
      a: function a(evt) {
        var res = {};
        var onBack = 'onBack';
        if (__isObject(_CACHE[onBack])) {
          _CACHE[onBack].event = evt;
        } else {
          _CACHE[onBack] = {
            event: evt,
            allowButton: true
          };
        }
        if (_CACHE[onBack].allowButton === false) {
          evt.preventDefault();
        }
        res.backAvailable = _CACHE[onBack].allowButton;
        return res;
      },

      e: {
        handleEventData: false
      }
    },
    offBack: {},

    onResume: {
      a: function a(evt) {
        var res = {};
        if (!__isUndefined(evt.data)) {
          res.data = evt.data;
        }
        if (__hasOwnProperty(evt.data, '___forResume___')) {
          res.data = evt.data.___forResume___;
        }
        return res;
      },

      e: {
        handleEventData: false
      }
    },
    offResume: {},

    onPause: {},
    offPause: {},

    onPageResume: {
      a: function a(evt) {
        var res = {};
        if (!__isUndefined(evt.data)) {
          res.data = evt.data;
        }
        if (__hasOwnProperty(evt.data, '___forResume___')) {
          res.data = evt.data.___forResume___;
        }
        return res;
      },

      e: {
        handleEventData: false
      }
    },
    offPageResume: {},
    onPagePause: {},
    offPagePause: {},

    onTitleClick: {},
    offTitleClick: {},

    //onSubTitleClick: {},
    onPullDownRefresh: {
      m: 'firePullToRefresh',
      a: function a(evt) {
        var res = {};
        var onPDR = 'onPullDownRefresh';
        if (__isObject(_CACHE[onPDR])) {
          _CACHE[onPDR].event = evt;
        } else {
          _CACHE[onPDR] = {
            event: evt,
            allow: true
          };
        }
        if (_CACHE[onPDR].allow === false) {
          _CACHE[onPDR].event.preventDefault();
        }
        res.refreshAvailable = _CACHE[onPDR].allow;
        return res;
      },

      e: {
        handleEventData: false
      }
    },
    offPullDownRefresh: {
      m: 'firePullToRefresh'
    },

    onNetworkChange: {
      d: function d(_opt, _cb, opt, cb) {
        //ç›´æŽ¥è°ƒç”¨ä¸€æ¬¡ getNetworkType åå›žå½“å‰ç½‘ç»œçŠ¶æ€
        var handler = function handler() {
          return AP.getNetworkType(_cb);
        };
        _cacheEventHandler('h5NetworkChange', cb, handler);
        AP.on('h5NetworkChange', handler);
      }
    },
    offNetworkChange: {
      d: function d(_opt, _cb, opt, cb) {
        _removeEventHandler('h5NetworkChange', cb);
      }
    },
    onAccelerometerChange: {
      b: function b() {
        _JS_BRIDGE.call('watchShake', { monitorAccelerometer: true });
      },
      a: function a(evt) {
        var res = {};
        _mapping(res, {
          x: 'x',
          y: 'y',
          z: 'z'
        }, __isObject(evt.data) ? evt.data : evt);
        return res;
      },

      e: {
        handleEventData: false
      }
    },
    offAccelerometerChange: {
      b: function b() {
        _JS_BRIDGE.call('watchShake', { monitorAccelerometer: false });
      }
    },
    onCompassChange: {
      b: function b() {
        _JS_BRIDGE.call('watchShake', { monitorCompass: true });
      },
      a: function a(evt) {
        var res = {};
        _mapping(res, {
          direction: 'direction'
        }, __isObject(evt.data) ? evt.data : evt);
        return res;
      },

      e: {
        handleEventData: false
      }
    },
    offCompassChange: {
      b: function b() {
        _JS_BRIDGE.call('watchShake', { monitorCompass: false });
      }
    },

    onBackgroundAudioPlay: {
      b: function b(opt) {
        _CACHE.getBAPSI.on();
        return opt;
      }
    },
    offBackgroundAudioPlay: {},

    onBackgroundAudioPause: {
      b: function b(opt) {
        _CACHE.getBAPSI.on();
        return opt;
      }
    },
    offBackgroundAudioPause: {},

    onBackgroundAudioStop: {
      b: function b(opt) {
        _CACHE.getBAPSI.on();
        return opt;
      }
    },
    offBackgroundAudioStop: {},

    onAppResume: {},
    offAppResume: {},
    onAppPause: {},
    offAppPause: {},

    ///////////////////////////// device /////////////////////////////
    getNetworkType: {
      a: function a(res) {
        if (!__isUndefined(res.networkInfo)) {
          res.networkType = __tuc(res.networkInfo);
        }
        //æ— éœ€è¿™ä¹ˆå¤šå­—æ®µ
        delete res.err_msg;
        delete res.networkInfo;
        return res;
      }
    },
    scan: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'type'
        });
        opt.type = opt.type || 'qr';
        return opt;
      },
      a: function a(res) {
        if (res.qrCode || res.barCode) {
          res.code = res.qrCode || res.barCode;
          delete res.qrCode;
          delete res.barCode;
        }

        return res;
      }
    },
    watchShake: {
      b: function b(opt) {
        //ç”¨æˆ·çœŸæ­£ä½¿ç”¨æ­¤æŽ¥å£æ—¶ä¸éœ€è¦ä¼ å…¥ä»»ä½•å‚æ•°
        //ç§»é™¤æ‰€æœ‰å…¥å‚ï¼Œå…¥å‚è¢«ä¼ æ„Ÿå™¨äº‹ä»¶ç›‘å¬å¼€å…³å ç”¨
        //å¦‚æžœæœ‰å…¥å‚ï¼Œios ä¸ä¼šè°ƒç”¨å›žè°ƒï¼Œandroid ä¼šç›´æŽ¥è°ƒç”¨å›žè°ƒã€‚
        if (__isEmptyObject(opt)) {
          opt = null;
        }
        return opt;
      }
    },
    getLocation: {
      b: function b(opt) {
        _mapping(opt, {
          accuracy: 'horizontalAccuracy',
          type: 'requestType%d'
        });
        if (__isUndefined(opt.requestType)) {
          opt.requestType = 2;
        }
        if (__isAndroid()) {
          if (__isUndefined(opt.isHighAccuracy)) {
            opt.isHighAccuracy = true;
          }
          if (__isUndefined(opt.isNeedSpeed)) {
            opt.isNeedSpeed = true;
          }
        }
        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          citycode: 'cityCode',
          adcode: 'adCode'
        });
        if (__isUndefined(res.city) && res.province) {
          res.city = res.province;
        }
        if (res.latitude) {
          res.latitude = _toType('%s', res.latitude);
        }
        if (res.longitude) {
          res.longitude = _toType('%s', res.longitude);
        }
        if (res.accuracy) {
          res.accuracy = _toType('%f', res.accuracy);
        }
        if (res.speed) {
          res.speed = _toType('%f', res.speed);
        }
        return res;
      }
    },
    getSystemInfo: {
      a: function a(res) {
        var pixelRatio = 'pixelRatio';
        var windowWidth = 'windowWidth';
        var windowHeight = 'windowHeight';
        var language = 'language';
        if (!__hasOwnProperty(res, 'error')) {
          res[pixelRatio] = _toType('%f', res[pixelRatio]);
          res[windowWidth] = _toType('%d', res[windowWidth]);
          res[language] = (res[language] || '').replace(/\s?\w+\/((?:\w|-)+)$/, '$1');
          res[windowHeight] = _toType('%d', res[windowHeight]);
          try {
            if (__isIOS() && AP.compareVersion('10.0.12') < 0) {
              res[windowHeight] = window.screen.height - 64;
            }
          } catch (err) {}
        }
        return res;
      }
    },
    vibrate: {},
    getServerTime: {},

    /////////////////////////// media //////////////////////////
    previewImage: {
      m: 'imageViewer',
      /**
       * æŽ¥å£æ”¹é€  opt.current => opt.init
       *        opt.urls => opt.images
       *        é»˜è®¤æ”¯æŒç›´æŽ¥ä¼ å…¥ä¸€ä¸ªæ•°ç»„ä½œä¸º opt.urls
       */
      b: function b(opt) {
        _mapping(opt, {
          _: 'urls',
          current: 'init%d'
        });
        //å¤„ç†é»˜è®¤ç´¢å¼•
        if (__isUndefined(opt.init)) {
          opt.init = 0;
        }
        //å¤„ç†å›¾ç‰‡é“¾æŽ¥
        opt.images = [];
        (opt.urls || []).forEach(function (url) {
          opt.images.push({
            u: url
          });
        });
        delete opt.urls;

        return opt;
      }
    },
    chooseImage: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'count%d'
        });
        if (__isUndefined(opt.count)) {
          opt.count = 1;
        }
        if (__isString(opt.sourceType)) {
          opt.sourceType = [opt.sourceType];
        }
        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          errorCode: 'error',
          errorDesc: 'errorMessage',
          localIds: 'apFilePaths',
          tempFilePaths: 'apFilePaths'
        });
        //åˆ é™¤æ— ç”¨å±žæ€§
        delete res.scene;
        delete res.localIds;
        delete res.tempFilePaths;

        //android è¿”å›žå­—ç¬¦ä¸²
        if (__isString(res.apFilePaths)) {
          res.apFilePaths = __parseJSON(res.apFilePaths);
        }

        return res;
      }
    },
    chooseVideo: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'maxDuration%d'
        });
        if (__isString(opt.sourceType)) {
          opt.sourceType = [opt.sourceType];
        }
        if (__isString(opt.camera)) {
          opt.camera = [opt.camera];
        }
        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          errorCode: 'error', //android errorCode
          errorDesc: 'errorMessage', // android errorDesc
          msg: 'errorMessage', // ios msg
          localId: 'apFilePath',
          tempFilePath: 'apFilePath',
          tempFile: 'apFilePath'
        });
        //åˆ é™¤æ— ç”¨å±žæ€§
        delete res.localId;
        delete res.tempFilePath;
        delete res.tempFile;

        switch (res.error) {
          case 0:
            //ios æˆåŠŸ
            delete res.error;
            break;
          case 1:
            //ios å‚æ•°å‡ºé”™
            res.error = 2; //é€šç”¨å‚æ•°æ— æ•ˆ
            break;
          case 2:
            //ios ç”¨æˆ·å–æ¶ˆ
            res.error = 10; //android ç”¨æˆ·å–æ¶ˆ
            break;
          case 3:
            //ios æ“ä½œå¤±è´¥
            res.error = 11; //android æ“ä½œå¤±è´¥
            break;
          case 4:
            //ios æ•°æ®å¤„ç†å¤±è´¥
            res.error = 12;
            break;
          default:
        }

        return res;
      }
    },
    uploadFile: {
      b: function b(opt) {
        _mapping(opt, {
          headers: 'header',
          fileName: 'name',
          fileType: 'type'
        });
        if (_isLocalId(opt.filePath)) {
          opt.localId = opt.filePath;
          delete opt.filePath;
        }
        return opt;
      },
      a: function a(res) {
        if (res.error === 2) {
          res.error = 11;
        }
        return res;
      }
    },
    saveImage: {
      b: function b(opt, cb) {
        _mapping(opt, {
          _: 'url',
          url: 'src'
        });
        if (__isFunction(cb)) {
          opt.cusHandleResult = true;
        }
        return opt;
      }
    },
    downloadFile: {
      b: function b(opt) {
        _mapping(opt, {
          headers: 'header'
        });
        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          tempFilePath: 'apFilePath',
          errorCode: 'error'
        });
        delete res.tempFilePath;
        return res;
      }
    },

    ///////////////////////////////// æ•°æ® ////////////////////////////////
    setSessionData: {
      b: function b(opt) {
        opt = _fixOptData(opt);
        if (!__isObject(opt.data)) {
          opt.data = {
            data: opt.data
          };
        }
        __forEach(opt.data, function (key, value) {
          opt.data[key] = JSON.stringify(value);
        });
        return opt;
      }
    },
    getSessionData: {
      b: function b(opt) {
        //ç›´æŽ¥ä¼ å…¥ä¸€ä¸ª key
        if (__isString(opt._)) {
          opt.keys = [opt._];
        }
        //ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªæ•°ç»„
        if (__isArray(opt._)) {
          opt.keys = opt._;
        }
        delete opt._;
        return opt;
      },
      a: function a(res) {
        __forEach(res.data, function (key, value) {
          res.data[key] = __parseJSON(value);
        });
        return res;
      }
    },
    ////////////////////////////// å¼€æ”¾æŽ¥å£ ////////////////////////////////
    startBizService: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'name',
          params: 'param%s'
        });
        return opt;
      }
    },
    tradePay: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'orderStr'
        });
        return opt;
      }
    },
    getAuthCode: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'scopes'
        });
        if (__isString(opt.scopes)) {
          opt.scopeNicks = [opt.scopes];
        } else if (__isArray(opt.scopes)) {
          opt.scopeNicks = opt.scopes;
        } else {
          opt.scopeNicks = ['auth_base'];
        }
        delete opt.scopes;

        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          authcode: 'authCode'
        });
        return res;
      }
    },
    getAuthUserInfo: {
      a: function a(res) {
        _mapping(res, {
          nick: 'nickName',
          userAvatar: 'avatar'
        });
        return res;
      }
    },
    ////////////////////////// v0.1.3+ ///////////////////////////////
    openInBrowser: {
      /**
       * æŽ¥å£å¯ç›´æŽ¥ä¼ å…¥ä¸€ä¸ªå­—ç¬¦ä¸²ï¼ˆopt.urlï¼‰
       */
      b: function b(opt) {
        return _mapping(opt, {
          _: 'url'
        });
      }
    },
    openLocation: {
      b: function b(opt) {
        if (__isUndefined(opt.scale)) {
          opt.scale = 15; //é»˜è®¤ç¼©æ”¾15çº§
        }
        return opt;
      }
    },
    showPopMenu: {
      b: function b(opt) {
        //å…¶ä»–å±žæ€§æ˜ å°„
        _mapping(opt, {
          _: 'items',
          items: 'menus'
        });

        //popMenuClickäº‹ä»¶åªç›‘å¬ä¸€æ¬¡ï¼Œé˜²æ­¢å¤šæ¬¡å›žè°ƒ
        if (__isObject(_CACHE.showPopMenu)) {
          _CACHE.showPopMenu.menus = {};
        } else {
          _CACHE.showPopMenu = {
            menus: {}
          };
        }
        if (__isArray(opt.menus)) {
          var menus = opt.menus;
          //éœ€è¦å¤åˆ¶ä¸€ä»½ï¼Œä¸èƒ½åœ¨åŽŸæ•°ç»„ä¸Šä¿®æ”¹ï¼Œä¼šç ´åç”¨æˆ·æ•°æ®
          opt.menus = [];
          menus.forEach(function (item, i) {
            //æ”¯æŒèœå•ç›´æŽ¥æ˜¯ä¸ªå­—ç¬¦ä¸²æ•°ç»„
            if (__isString(item)) {
              item = {
                title: item
              };
            }
            item = _mapping(__extend({}, item), {
              title: 'name%s',
              tag: 'tag%s',
              badge: 'redDot%s'
            }, {
              tag: i,
              title: item.title,
              badge: __isUndefined(item.badge) ? '-1' : item.badge
            });
            if (!__isUndefined(item.icon)) {
              item.icon = _toType('%b', item.icon);
            }
            opt.menus.push(item);
            _CACHE.showPopMenu.menus[item.name] = i;
          });
        }
        return opt;
      },
      d: function d(_opt, cb) {
        var apiName = 'showPopMenu';
        if (_CACHE.showPopMenu.onEvent !== true) {
          _CACHE.showPopMenu.onEvent = true;
          //ç›‘å¬ç‚¹å‡»äº‹ä»¶
          AP.on('popMenuClick', function (evt) {
            var res = {};
            _mapping(res, {
              title: 'index%d'
            }, {
              title: __isObject(evt.data) && evt.data.title ? _CACHE.showPopMenu.menus[evt.data.title] : '-1'
            });
            cb(res);
          });
        }

        //è°ƒç”¨æ–¹æ³•
        _JS_BRIDGE.call(apiName, _opt, function (result) {
          //result å¹¶éžçœŸæ­£çš„è¿”å›žå€¼ï¼Œä½†æ˜¯è¦å¤„ç†æŽ¥å£é”™è¯¯
          _handleApiError(apiName, result);
        });
      }
    },
    setOptionButton: {
      m: 'setOptionMenu',
      b: function b(opt) {
        if (__isString(opt._)) {
          opt.title = opt._;
          delete opt._;
        }
        if (__isArray(opt._)) {
          opt.items = opt._;
          delete opt._;
        }
        _mapping(opt, {
          items: 'menus',
          type: 'iconType',
          badge: 'redDot%s'
        });
        if (!__isUndefined(opt.icon)) {
          opt.icon = _toType('%b', opt.icon);
        }
        //optionMenuäº‹ä»¶åªç›‘å¬ä¸€æ¬¡ï¼Œé˜²æ­¢å¤šæ¬¡å›žè°ƒ
        if (__isObject(_CACHE.setOptionButton)) {
          _CACHE.setOptionButton.menus = [];
        } else {
          _CACHE.setOptionButton = {
            menus: []
          };
        }
        if (__isArray(opt.menus)) {
          var menus = opt.menus;
          //éœ€è¦å¤åˆ¶ä¸€ä»½ï¼Œä¸èƒ½åœ¨åŽŸæ•°ç»„ä¸Šä¿®æ”¹ï¼Œä¼šç ´åç”¨æˆ·æ•°æ®
          opt.menus = [];
          menus.forEach(function (item, i) {
            item = _mapping(__extend({}, item), {
              type: 'icontype',
              badge: 'redDot%s'
            }, {
              badge: __isUndefined(item.badge) ? '-1' : item.badge
            });
            if (!__isUndefined(item.icon)) {
              item.icon = _toType('%b', item.icon);
            }
            opt.menus.unshift(item);
            _CACHE.setOptionButton.menus[menus.length - 1 - i] = i;
          });
          if (opt.menus.length > 0 && __isUndefined(opt.override)) {
            opt.override = true;
          }
        }
        //æ¯æ¬¡ setOptionMenu è¦æ³¨å†Œæ–°çš„äº‹ä»¶
        if (__isFunction(_CACHE.setOptionButton.onEvent)) {
          AP.off('optionMenu', _CACHE.setOptionButton.onEvent);
        }
        if (__isFunction(opt.onClick)) {
          var onClick = opt.onClick;
          var eventHandler = function eventHandler(evt) {
            var index = 0;
            var res = {};
            if (__isObject(evt.data) && __isNumber(evt.data.index) && _CACHE.setOptionButton.menus.length > 0) {
              index = _CACHE.setOptionButton.menus[evt.data.index];
            }
            res.index = _toType('%d', index);
            onClick(res);
          };
          _CACHE.setOptionButton.onEvent = eventHandler;
          //ç›‘å¬ç‚¹å‡»äº‹ä»¶
          if (opt.reset !== true) {
            AP.on('optionMenu', eventHandler);
          }
          delete opt.onClick;
        }
        return opt;
      },
      d: function d(_opt, cb) {
        _JS_BRIDGE.call('setOptionMenu', _opt, cb);
        //iOS æ²¡æœ‰å›žè°ƒ, 10.0.8
        if (__isIOS()) {
          _fakeCallBack(cb, {});
        }
        AP.showOptionButton();
      }
    },
    showOptionButton: {
      m: 'showOptionMenu'
    },
    hideOptionButton: {
      m: 'hideOptionMenu'
    },
    showBackButton: {},
    hideBackButton: {},
    allowBack: {
      d: function d(opt) {
        var onBack = 'onBack';
        _mapping(opt, {
          _: 'allowButton'
        });
        opt.allowButton = __isUndefined(opt.allowButton) ? true : !!opt.allowButton;

        if (__isBoolean(opt.allowGesture)) {
          _JS_BRIDGE.call('setGestureBack', {
            val: opt.allowGesture
          });
        }
        if (__isObject(_CACHE[onBack])) {
          _CACHE[onBack].allowButton = opt.allowButton;
        } else {
          _CACHE[onBack] = {
            allowButton: opt.allowButton
          };
          AP.onBack();
        }
        if (opt.allowButton === false && _CACHE[onBack].event) {
          _CACHE[onBack].event.preventDefault();
        }
      }
    },
    startRecord: {
      m: 'startAudioRecord',
      b: function b(opt) {
        _mapping(opt, {
          maxDuration: 'maxRecordTime%f',
          minDuration: 'minRecordTime%f',
          bizType: 'business'
        }, {
          maxDuration: opt.maxDuration || 60,
          minDuration: opt.minDuration || 1
        });
        if (__isUndefined(opt.business)) {
          opt.business = _MEDIA_BUSINESS;
        }
        // 10.0.5ç»Ÿä¸€æˆç§’
        // opt.maxRecordTime *= 1000;
        // opt.minRecordTime *= 1000;
        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          tempFilePath: 'apFilePath',
          identifier: 'apFilePath'
        });
        return res;
      }
    },
    stopRecord: {
      m: 'stopAudioRecord'
    },
    cancelRecord: {
      m: 'cancelAudioRecord'
    },
    playVoice: {
      m: 'startPlayAudio',
      b: function b(opt) {
        _mapping(opt, {
          _: 'filePath',
          filePath: 'identifier',
          bizType: 'business'
        });
        if (__isUndefined(opt.business)) {
          opt.business = _MEDIA_BUSINESS;
        }
        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          identifier: 'filePath'
        });
        return res;
      }
    },
    pauseVoice: {
      m: 'pauseAudioPlay'
    },
    resumeVoice: {
      m: 'resumeAudioPlay'
    },
    stopVoice: {
      m: 'stopAudioPlay'
    },
    makePhoneCall: {
      d: function d(opt, cb) {
        var url = 'tel:';
        _mapping(opt, {
          _: 'number'
        });
        url += opt.number;
        _JS_BRIDGE.call('openInBrowser', { url: url }, cb);
      }
    },
    playBackgroundAudio: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'url',
          url: 'audioDataUrl%s',
          title: 'audioName%s',
          singer: 'singerName%s',
          describe: 'audioDescribe%s',
          logo: 'audioLogoUrl%s',
          cover: 'coverImgUrl%s',
          bizType: 'business'
        }, {
          bizType: opt.bizType || _MEDIA_BUSINESS
        });
        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          describe: 'errorMessage'
        });
        _handleResultSuccess(res, 12, 0);
        return res;
      }
    },
    pauseBackgroundAudio: {
      a: function a(res) {
        _mapping(res, {
          describe: 'errorMessage'
        });
        _handleResultSuccess(res, 12, 0);
        return res;
      }
    },
    stopBackgroundAudio: {
      a: function a(res) {
        _mapping(res, {
          describe: 'errorMessage'
        });
        _handleResultSuccess(res, 12, 0);
        return res;
      }
    },
    seekBackgroundAudio: {
      b: function b(opt) {
        _mapping(opt, {
          _: 'position',
          bizType: 'business'
        }, {
          bizType: opt.bizType || _MEDIA_BUSINESS
        });
        opt.position = _toType('%f', opt.position);
        return opt;
      },
      a: function a(res) {
        _mapping(res, {
          describe: 'errorMessage'
        });
        _handleResultSuccess(res, 12, 0);
        return res;
      }
    },
    getBackgroundAudioPlayerState: {
      a: function a(res) {
        _mapping(res, {
          audioDataUrl: 'url',
          describe: 'errorMessage'
        });
        _handleResultSuccess(res, 12, 0);
        return res;
      }
    }

    //////////////////////////// æœªå¼€æ”¾æ–¹æ³• //////////////////////////////

    //numInput: {},
    //inputFocus: {},
    //inputBackFill: {},
    //numInputReset: {},
    //inputBlur: {},
    //downloadApp: {},
    //getSwitchControlStatus: {},
    //setToolbarMenu: {},


    //uploadImage: {}, ï¼ŸapFilePath
    //downloadImage: {}, ï¼ŸapFilePath
    //saveFile: {},
    //rpc: {},
    //startApp: {},
    //remoteLog: {},
    //getConfig: {},
    //getUserInfo: {},
    //setSharedData: {},
    //getSharedData: {},
    //removeSharedData: {},
    //setClipboard: {},
    //getClipboard: {},
    //login: {},
    //sendSMS: {},
    //isSupportShortCut: {},
    //setShortCut: {},
    //removeShortCut: {},
    //registerSync: {},
    //responseSyncNotify: {},
    //unregisterSync: {},
    //refreshSyncSkey: {},
    //getScreenBrightness: {},
    //setScreenBrightness: {},
    //isInstalledApp: {},
    //getAllContacts: {},
    //preRender: {},
    //finishRender: {},
    //clearRender: {},


    //setPullDownText: {},
    //hideTransBack: {},
    //limitAlert: {},
    //startPackage: {},
    //getClientInfo: {},
    //reportData: {},
    //getSceneStackInfo: {},
    //getAppInfo: {},
    //rsa: {},
    //shareToken: {},
    //snapshot: {},
    //getAppToken: {},
    //ping: {},
    //checkJSAPI: {},
    //checkApp: {},
    //commonList: {},
    //beehiveOptionsPicker: {},
    //beehiveGetPOI: {},
    //addEventCal: {},
    //removeEventCal: {},
    //speech: {},
    //selectAddress: {},
    //nfch5plugin: {},
  };
  /********************* AP å¯¹è±¡å…¶ä»–é™æ€å±žæ€§åŠåŒæ­¥æ–¹æ³• ************************/

  //Alipay ç¼©å†™
  var AP = {
    version: '3.1.0',
    ua: _UA,
    isAlipay: __inUA(/AlipayClient/),
    alipayVersion: function () {
      var version = _UA.match(/AlipayClient[a-zA-Z]*\/(\d+(?:\.\d+)+)/);
      return version && version.length ? version[1] : '';
    }(),
    /////////////////////////////// AP åŒæ­¥æ–¹æ³• /////////////////////////////
    /**
     * ç‰ˆæœ¬æ¯”è¾ƒ
     * @method compareVersion
     * @param  {String}       targetVersion ç›®æ ‡ç‰ˆæœ¬
     * @return {Number}                     æ¯”è¾ƒç»“æžœï¼Œ1ä»£è¡¨å½“å‰ç‰ˆæœ¬å¤§äºŽç›®æ ‡ç‰ˆæœ¬ï¼Œ-1ç›¸åï¼Œç›¸åŒä¸º0
     */
    compareVersion: function compareVersion(targetVersion) {
      var alipayVersion = AP.alipayVersion.split('.');

      targetVersion = targetVersion.split('.');
      for (var i = 0, n1, n2; i < alipayVersion.length; i++) {
        n1 = parseInt(targetVersion[i], 10) || 0;
        n2 = parseInt(alipayVersion[i], 10) || 0;
        if (n1 > n2) return -1;
        if (n1 < n2) return 1;
      }
      return 0;
    },

    /**
     * èŽ·å– url ä¸Šçš„å…¨éƒ¨ä¼ å‚å¹¶è½¬æˆå¯¹è±¡
     * @method parseQueryString
     * @param  {String}          queryString
     * @return {Object}          location.search å¯¹åº”çš„é”®å€¼å¯¹è±¡
     */
    parseQueryString: function parseQueryString(queryString) {
      var result = {};
      var searchStr = queryString || window.location.search;
      var bool = {
        true: true,
        false: false
      };
      var kv;
      searchStr = searchStr.indexOf('?') === 0 ? searchStr.substr(1) : searchStr;
      searchStr = searchStr ? searchStr.split('&') : '';
      for (var i = 0; i < searchStr.length; i++) {
        kv = searchStr[i].split('=');
        kv[1] = decodeURIComponent(kv[1]);
        //Boolean
        kv[1] = __isUndefined(bool[kv[1]]) ? kv[1] : bool[kv[1]];
        //Number
        //kv[1] = +kv[1] + '' === kv[1] ? +kv[1] : kv[1];
        result[kv[0]] = kv[1];
      }
      _apiRemoteLog('parseQueryString');
      return result;
    },

    /**
     * å¼€å¯ debug æ¨¡å¼ï¼ŒæŽ§åˆ¶å°æ‰“å°æŽ¥å£è°ƒç”¨æ—¥å¿—
     * @type {Object}
     */
    enableDebug: function enableDebug() {
      AP.debug = true;
    },


    /**
     * ç»‘å®šå…¨å±€äº‹ä»¶
     * @method on
     * @param  {String}   evts äº‹ä»¶ç±»åž‹ï¼Œå¤šä¸ªäº‹ä»¶ç”¨ç©ºæ ¼åˆ†éš”
     * @param  {Function} fn     äº‹ä»¶å›žè°ƒ
     */
    on: function on(evts, fn) {
      var isReady = evts === 'ready';
      var isSimple = isReady || evts === 'back';

      if (isSimple) {
        document.addEventListener(isReady ? _JS_BRIDGE_NAME + 'Ready' : evts, fn, false);
      } else {
        evts = evts.replace(/ready/, _JS_BRIDGE_NAME + 'Ready');
        evts.split(/\s+/g).forEach(function (eventName) {
          document.addEventListener(eventName, fn, false);
        });
      }
    },

    /**
     * ç§»é™¤äº‹ä»¶ç›‘å¬
     * @method off
     * @param  {String}   evt    äº‹ä»¶ç±»åž‹
     * @param  {Function} fn     äº‹ä»¶å›žè°ƒ
     */
    off: function off(evt, fn) {
      document.removeEventListener(evt, fn, false);
    },
    trigger: function trigger(evtName, data) {
      var evt = document.createEvent('Events');
      evt.initEvent(evtName, false, true);
      evt.data = data || {};
      document.dispatchEvent(evt);
      return evt;
    },


    /**
     * readyäº‹ä»¶ç‹¬ç«‹æ–¹æ³•
     * @method ready
     * @param  {Function} fn ready å›žè°ƒ
     */
    ready: function ready(fn) {
      if (__isSupportPromise()) {
        return new Promise(realReady);
      } else {
        realReady();
      }

      function realReady(resolve) {
        if (_isBridgeReady()) {
          if (__isFunction(fn)) {
            fn();
          }
          if (__isFunction(resolve)) {
            resolve();
          }
        } else {
          AP.on('ready', function () {
            //é˜²æ­¢ jsbridge æ™šæ³¨å…¥
            _isBridgeReady();

            if (__isFunction(fn)) {
              fn();
            }
            if (__isFunction(resolve)) {
              resolve();
            }
          });
        }
      }
    },



    /**
     * é€šç”¨æŽ¥å£ï¼Œè°ƒç”¨æ–¹å¼ç­‰åŒAlipayJSBridge.call
     * æ— éœ€è€ƒè™‘readyäº‹ä»¶ï¼Œä¼šè‡ªåŠ¨åŠ å…¥åˆ°å¾…æ‰§è¡Œé˜Ÿåˆ—
     * @method call
     */
    call: function call() {
      var args = __argumentsToArg(arguments);
      if (__isSupportPromise()) {
        return AP.ready().then(function () {
          return new Promise(realCall);
        });
      } else {
        //å¦‚æžœç›´æŽ¥åŠ åˆ° ready äº‹ä»¶é‡Œä¼šæœ‰ä¸è§¦å‘è°ƒç”¨çš„æƒ…å†µ
        //AP.ready(realCall);

        if (_isBridgeReady()) {
          realCall();
        } else {
          //ä¿å­˜åœ¨å¾…æ‰§è¡Œé˜Ÿåˆ—
          _WAITING_QUEUE.push(args);
        }
      }

      function realCall(resolve, reject) {
        var apiName;
        var opt; //åŽŸå§‹ option
        var cb; //åŽŸå§‹ callback
        var _opt; //å¤„ç†è¿‡çš„ option
        var _cbSFC; //ä¸åŒçŠ¶æ€å›žè°ƒ
        var _cb; //å¤„ç†è¿‡çš„ callback
        var onEvt;
        var offEvt;
        var doingFn;
        var logOpt;
        //å¼ºåˆ¶è½¬ä¸º name + object + function å½¢å¼çš„å…¥å‚
        apiName = args[0] + '';
        opt = args[1];
        cb = args[2];
        //å¤„ç† cb å’Œ opt çš„é¡ºåº
        if (__isUndefined(cb) && __isFunction(opt)) {
          cb = opt;
          opt = {};
        }
        //æŽ¥å£æœ‰éžå¯¹è±¡å…¥å‚ï¼Œè®¾ä¸ºå¿«æ·å…¥å‚
        if (!__isObject(opt) && args.length >= 2) {
          //beforeã€doingã€after æ–¹æ³•ä¸­ç›´æŽ¥å– opt._ ä½œä¸ºå‚æ•°
          opt = {
            _: opt
          };
        }
        //å…œåº•
        if (__isUndefined(opt)) {
          opt = {};
        }

        //å¤„ç†å…¥å‚
        _opt = _getApiOption(apiName, opt, cb);

        //èŽ·å–å›žè°ƒ
        _cbSFC = _getApiCallBacks(apiName, _opt);

        if (__isUndefined(_opt)) {
          console.error('please confirm ' + apiName + '.before() returns the options.');
        }
        //èŽ·å– api çš„ d æ–¹æ³•
        doingFn = _getApiDoing(apiName);

        //è¾“å‡ºå…¥å‚
        logOpt = __hasOwnProperty(opt, '_') ? opt._ : opt;
        _apiLog(apiName, logOpt, _opt);

        //æ˜¯å¦æ˜¯äº‹ä»¶ç›‘å¬
        onEvt = _getApiOnEvent(apiName);
        //æ˜¯å¦æ˜¯äº‹ä»¶ç§»é™¤
        offEvt = _getApiOffEvent(apiName);

        //å¤„ç†å›žè°ƒ
        _cb = function _cb(res) {
          var _res = void 0;
          res = res || {};

          if (onEvt && _getApiExtra(apiName, 'handleEventData') !== false) {
            _res = _handleEventData(res);
          }

          //å¤„ç†ç»“æžœ
          _res = _getApiResult(apiName, _res || res, _opt, opt, cb);
          if (__isUndefined(_res)) {
            console.error('please confirm ' + apiName + '.after() returns the result.');
          }
          //å¤„ç†é”™è¯¯ç 
          _res = _handleApiError(apiName, _res);
          //æ‰“å° debug æ—¥å¿—
          _apiLog(apiName, logOpt, _opt, res, _res);

          if (__hasOwnProperty(_res, 'error') || __hasOwnProperty(_res, 'errorMessage')) {
            if (__isFunction(reject)) {
              reject(_res);
            }
            if (__isFunction(_cbSFC.fail)) {
              _cbSFC.fail(_res);
            }
          } else {
            if (__isFunction(resolve)) {
              resolve(_res);
            }
            if (__isFunction(_cbSFC.success)) {
              _cbSFC.success(_res);
            }
          }
          if (__isFunction(_cbSFC.complete)) {
            _cbSFC.complete(_res);
          }
          //æ‰§è¡Œç”¨æˆ·çš„å›žè°ƒ
          if (__isFunction(cb)) {
            cb(_res);
          }
        };

        //å¦‚æžœå­˜åœ¨ d ç›´æŽ¥æ‰§è¡Œï¼Œå¦åˆ™æ‰§è¡Œ AlipayJSBridge.call
        if (__isFunction(doingFn)) {
          doingFn(_opt, _cb, opt, cb);
        } else if (onEvt) {
          _cacheEventHandler(onEvt, cb, _cb, _cbSFC);
          AP.on(onEvt, _cb);
        } else if (offEvt) {
          _removeEventHandler(offEvt, cb);
        } else {
          _JS_BRIDGE.call(_getApiName(apiName), _opt, _cb);
        }
        _apiRemoteLog(apiName);
      }
    },

    /**
     * æ‰©å±• JSAPI çš„æŽ¥å£
     */
    extendJSAPI: function extendJSAPI(JSAPI, isInitAP) {
      //å¦‚æžœæ˜¯å­—ç¬¦ä¸²ï¼Œç›´æŽ¥å½“ä½œæŽ¥å£å
      if (!isInitAP && __isString(JSAPI)) {
        JSAPI = [JSAPI];
      }
      __forEach(JSAPI, function (key) {
        var apiName = key;
        // å¦‚æžœæ˜¯åˆå§‹åŒ–è°ƒç”¨ï¼Œåˆ™æ— éœ€å†æ³¨å†Œåˆ° _JSAPI å¯¹è±¡ä¸Š
        if (isInitAP !== true) {
          var api = JSAPI[apiName];
          //å¦‚æžœæŽ¥å£å®šä¹‰æ˜¯ä¸€ä¸ª functionï¼Œå³ä½œä¸º doing æ–¹æ³•
          if (__isFunction(api)) {
            api = {
              doing: api
            };
          }
          if (__isString(api)) {
            apiName = api;
            api = {};
            api[apiName] = {};
          }

          _JSAPI[apiName] = _mapping(_JSAPI[apiName] || {}, {
            mapping: 'm',
            before: 'b',
            doing: 'd',
            after: 'a'
          }, api);

          if (__isObject(api.extra)) {
            _JSAPI[apiName].e = _JSAPI[apiName].e || {};
            _JSAPI[apiName].e = __extend(_JSAPI[apiName].e, api.extra);
          }
        }

        // TODO: éœ€è¦éªŒè¯U3æ˜¯å¦æ”¯æŒbindå‚æ•°
        AP[apiName] = AP.call.bind(null, apiName);
        // AP[apiName] = () => AP.call.apply(null, [apiName].concat(__argumentsToArg(arguments)));
      }, true);
    }
  };
  AP.extendJSAPI.mapping = _mapping;
  AP.extendJSAPI.toType = _toType;

  if (!AP.isAlipay) {
    console.warn('Run ' + 'alipayjsapi' + '.js in ' + 'Alipay' + ' please!');
  }
  /*********************** æ³¨å†Œå¼‚æ­¥ JSAPI ***********************/

  (function () {
    // å°† JSAPI æ³¨å†Œåˆ° AP ä¸Š
    AP.extendJSAPI(_JSAPI, true);
    //ready å…¥å£
    AP.on('ready', function () {
      if (!!_WAITING_QUEUE.length) {
        next();
      }
      function next() {
        __raf(function () {
          var args = _WAITING_QUEUE.shift();
          AP.call.apply(null, args);
          if (_WAITING_QUEUE.length) next();
        });
      }
    });
  })();
  /******************JSAPI ç›¸å…³è¾…åŠ©å¤„ç†æ–¹æ³• _ ********************/
  /**
   * æ˜¯å¦ready
   * @method _isBridgeReady
   * @return {Boolean} æ˜¯å¦ å¯ä»¥è°ƒç”¨ AlipayJSBridge.call
   */
  function _isBridgeReady() {
    _JS_BRIDGE = _JS_BRIDGE || self[_JS_BRIDGE_NAME];
    return _JS_BRIDGE && _JS_BRIDGE.call;
  }
  /**
   * èŽ·å–ç¼“å­˜ç›¸å…³æŽ¥å£çš„ business
   * @method _getStorageBusiness
   * @return {String} business
   */
  function _getStorageBusiness() {
    var href = self && self.location && self.location.href ? self.location.href : '';
    var business = href.replace(/^(http|https):\/\//i, '').split('/')[0];
    return business;
  }
  /**
   * å‡å›žè°ƒï¼Œç”¨äºŽæ²¡æœ‰å®žçŽ°å›žè°ƒçš„æŽ¥å£
   * @param {Function} cb
   * @param {Object} arg
   */
  function _fakeCallBack(cb, arg) {
    setTimeout(function () {
      cb(arg);
    }, 1);
  }
  /**
   * æ˜¯å¦æ˜¯ localId
   * @method _isLocalId
   * @param  {String}   localId èµ„æºå®šä½ç¬¦
   * @return {Boolean}          æ˜¯å¦ localId
   */
  function _isLocalId(localId) {
    return (/^[a-z0-9|]+$/i.test(localId)
    );
  }
  /**
   * æ˜¯å¦æ˜¯ apFilePath åœ°å€
   * @method _isApFilePath
   * @param  {String}      apFilePath 10.0.2æ–°ç»Ÿä¸€èµ„æºå®šä½ç¬¦
   * @return {Boolean}                æ˜¯å¦ apFilePath
   */
  // function _isApFilePath(apFilePath) {
  //   return /^https:\/\/resource\/[a-z0-9|]+\./i.test(apFilePath)
  // }


  /**
   * ä¿®å¤æŸä¸ªå¿«æ·å…¥å‚æ˜¯å¯¹è±¡ç±»åž‹
   * @method _fixOptData
   * @param  {Object}    opt     å…¥å‚å¯¹è±¡
   * @param  {String}    dataKey å¿«æ·å…¥å‚çš„ key
   * @return {Object}            å¯¹è±¡
   */
  function _fixOptData(opt, dataKey) {
    var objectArg = false;
    dataKey = dataKey || 'data';
    if (__hasOwnProperty(opt, '_')) {
      //å…¥å‚ä¸æ˜¯ä¸€ä¸ªå¯¹è±¡
      opt[dataKey] = opt._;
      delete opt._;
    } else {
      //å…¥å‚æ˜¯ä¸€ä¸ªå¯¹è±¡ï¼Œä½†å¯èƒ½æœ‰é™¤äº† data å¤–çš„å…¶ä»– key
      __forEach(opt, function (key) {
        if (key !== dataKey) {
          objectArg = true;
        }
      });
      if (objectArg) {
        objectArg = opt;
        opt = {};
        opt[dataKey] = objectArg;
      }
    }
    return opt;
  }

  /**
   * åˆ¤æ–­äº‹ä»¶æ³¨å†Œç›‘å¬æ˜¯å¦æ˜¯åŒä¸€ä¸ªå›žè°ƒï¼Œå¹¶è¿”å›žæ­¤å›žè°ƒå‡½æ•°
   * @method _getSameHandlers
   * @param  {String}        evt äº‹ä»¶å
   * @param  {Function}      cb  ç›¸åŒå›žè°ƒå‡½æ•°
   * @return {Function / false}            æ˜¯å¦æ˜¯å›žè°ƒ
   */
  function _getSameHandlers(evt, cb, isRemoveCache) {
    var sameHandlers = false;
    var sameIndex;
    if (!__isUndefined(evt)) {
      if (!_CACHE.EVENTS) {
        _CACHE.EVENTS = {};
      }
      if (!_CACHE.EVENTS[evt]) {
        _CACHE.EVENTS[evt] = {
          callbacks: []
        };
      }
      if (!_CACHE.EVENTS[evt].callbacks) {
        _CACHE.EVENTS[evt].callbacks = [];
      }

      _CACHE.EVENTS[evt].callbacks.forEach(function (item, i) {
        if (item.cb === cb) {
          sameHandlers = item;
          sameIndex = i;
        }
      });
      if (isRemoveCache && __isNumber(sameIndex)) {
        _CACHE.EVENTS[evt].callbacks.splice(sameIndex, 1);
      }
    }
    return sameHandlers;
  }

  function _cacheEventHandler(evt, cb, _cb, _cbSFC) {
    var sameCBs = _getSameHandlers(evt, cb);
    if (!sameCBs) {
      _CACHE.EVENTS[evt].callbacks.push({
        cb: cb,
        _cb: _cb,
        _cbSFC: _cbSFC
      });
    }
  }

  function _removeEventHandler(evt, cb) {
    var handlers = _getSameHandlers(evt, cb, true);
    if (!__isFunction(cb)) {
      //ç§»é™¤å…¨éƒ¨é€šè¿‡ AP.onXXXXæ³¨å†Œçš„ç›‘å¬
      _CACHE.EVENTS[evt].callbacks.forEach(function (item) {
        AP.off(evt, item._cb);
      });
      _CACHE.EVENTS[evt].callbacks = [];
    } else if (handlers) {
      AP.off(evt, handlers._cb);
    }
  }

  /**
   * èŽ·å–è¦æ³¨å†Œçš„äº‹ä»¶ç±»åž‹
   * @method _getApiOnEvent
   * @param  {String}     apiName API åç§°
   * @return {String}             äº‹ä»¶ç±»åž‹ï¼Œå¦‚æžœä¸æ˜¯äº‹ä»¶ç±» API å°±è¿”å›ž false
   */
  function _getApiOnEvent(apiName) {
    return _getApiEvent('on', apiName);
  }

  /**
   * èŽ·å–è¦ç§»é™¤çš„äº‹ä»¶ç±»åž‹
   * @method _getApiOffEvent
   * @param  {String}        apiName æŽ¥å£å
   * @return {String}                äº‹ä»¶ç±»åž‹ï¼Œå¦‚æžœä¸æ˜¯äº‹ä»¶ç±» API å°±è¿”å›ž false
   */
  function _getApiOffEvent(apiName) {
    return _getApiEvent('off', apiName);
  }

  /**
   * èŽ·å–äº‹ä»¶å
   * @method _getApiEvent
   * @param  {String}     prefix  å‰ç¼€
   * @param  {String}     apiName æŽ¥å£å
   * @return {String}             äº‹ä»¶å
   */
  function _getApiEvent(prefix, apiName) {
    var jsapi = _JSAPI[apiName];
    var evt = false;
    var evtApiPattern = prefix === 'off' ? /^off([A-Z])(\w+)/ : /^on([A-Z])(\w+)/;

    // ä»¥ onã€off å¼€å¤´çš„ api æ˜¯ äº‹ä»¶ï¼ŒæŽ’é™¤ AP.onã€AP.off æ–¹æ³•
    if (jsapi && evtApiPattern.test(apiName)) {
      apiName = apiName.match(evtApiPattern);
      evt = jsapi.m;
      if (!evt && apiName[1] && apiName[2]) {
        evt = __tlc(apiName[1]) + apiName[2];
      }
    }
    return evt;
  }

  /**
   * èŽ·å–æŽ¥å£æ‰©å±•å­—æ®µ
   * @method _getApiExtra
   * @param  {String}     apiName  æŽ¥å£å
   * @param  {String}     extraKey æ‰©å±•å­—æ®µçš„ key
   * @return {Any}                 è¿”å›žç›¸åº”å­—æ®µå€¼
   */
  function _getApiExtra(apiName, extraKey) {
    var jsapi = _JSAPI[apiName] || {};
    var extra = jsapi.e || jsapi.extra || {};
    return extra[extraKey];
  }
  /**
   * èŽ·å– opt._ï¼Œé€‚é…ç›´æŽ¥ä¼ å…¥æŸä¸ªå‚æ•°çš„åœºæ™¯ï¼Œå³è°ƒç”¨ api æ—¶ç¬¬äºŒä¸ªå‚æ•°ä¼ å…¥çš„ä¸æ˜¯ Object çš„æƒ…å†µ
   * @method _getObjArg
   * @param  {Object}   opt AP.call æ–¹æ³•çš„ opt å…¥å‚
   * @return {any}       ä¸€èˆ¬æ˜¯ Stringï¼Œé»˜è®¤æ˜¯ undefined
   */
  // function _getObjArg(opt, optTarget) {
  //   var arg = optTarget;
  //   if (!__isUndefined(opt._)) {
  //     arg = opt._;
  //     delete opt._;
  //   }
  //   return arg;
  // }

  /**
   * èŽ·å– JSAPI æ˜ å°„æŽ¥å£å
   * @method _getApiName
   * @param  {String}    apiName AP æŽ¥å£å
   * @return {String}            AlipayJSBridge æŽ¥å£å
   */
  function _getApiName(apiName) {
    var jsapi = _JSAPI[apiName];
    return jsapi && jsapi.m ? jsapi.m : apiName;
  }

  /**
   * å¤„ç† JSAPI çš„å…¥å‚
   * @method _getApiOption
   * @param  {String}      apiName JSAPI åç§°
   * @param  {Object}      opt     JSAPI å…¥å‚
   * @param  {Function}    cb      JSAPI æœªå¤„ç†è¿‡çš„å›žè°ƒå‡½æ•°
   * @return {Object}              å¤„ç†è¿‡çš„ opt
   */
  function _getApiOption(apiName, opt, cb) {
    var jsapi = _JSAPI[apiName];
    var finalOpt = jsapi && jsapi.b ? jsapi.b(__extend({}, opt), cb) : opt;
    var modifier = _getApiExtra(apiName, 'optionModifier');
    if (__isFunction(modifier)) {
      var modifyOpt = modifier(finalOpt, cb);
      if (__isObject(modifyOpt)) {
        finalOpt = modifyOpt;
      }
    }

    return finalOpt;
  }
  /**
   * èŽ·å–ä¸åŒçŠ¶æ€å›žè°ƒ
   * @method _getApiCallBacks
   * @param  {String}        apiName  æŽ¥å£å
   * @param  {Object}        opt      æŽ¥å£å…¥å‚
   * @return {Object}                 å›žè°ƒå¯¹è±¡
   */
  function _getApiCallBacks(apiName, opt) {
    var cb = {};
    opt = opt || {};
    if (__isFunction(opt.success)) {
      cb.success = opt.success;
      delete opt.success;
    }
    if (__isFunction(opt.fail)) {
      cb.fail = opt.fail;
      delete opt.fail;
    }
    if (__isFunction(opt.complete)) {
      cb.complete = opt.complete;
      delete opt.complete;
    }
    return cb;
  }

  /**
   * èŽ·å– API çš„ doing å‡½æ•°
   * @method _getApiDoing
   * @param  {String}     apiName API åç§°
   * @return {function}           doing å‡½æ•°
   */
  function _getApiDoing(apiName) {
    var jsapi = _JSAPI[apiName];
    return jsapi && jsapi.d ? jsapi.d : false;
  }

  /**
   * å¤„ç† JSAPI çš„å‡ºå‚
   * @method _getApiResult
   * @param  {String}      apiName JSAPI æŽ¥å£å
   * @param  {Object}      opt     JSAPI åŽŸå§‹å…¥å‚
   * @param  {Object}      _opt    JSAPI beforeæ–¹æ³• å¤„ç†è¿‡çš„å…¥å‚
   * @param  {Object}      res     JSAPI å‡ºå‚
   * @param  {Function}    cb      JSAPI æœªå¤„ç†è¿‡çš„å›žè°ƒå‡½æ•°
   * @return {Object}              å¤„ç†è¿‡çš„ res
   */
  function _getApiResult(apiName, res, _opt, opt, cb) {
    var jsapi = _JSAPI[apiName];
    var finalRes = jsapi && jsapi.a ? jsapi.a(__isEvent(res) ? res : __extend({}, res), _opt, opt, cb) : __extend({}, res);
    var modifier = _getApiExtra(apiName, 'resultModifier');
    if (__isFunction(modifier)) {
      var modifyRes = modifier(finalRes, _opt, opt, cb);
      if (__isObject(modifyRes)) {
        finalRes = modifyRes;
      }
    }
    return finalRes;
  }
  /**
   * å¤„ç†é”™è¯¯ä¿¡æ¯ï¼Œè½¬æ¢ error å­—æ®µä¸º Number ç±»åž‹
   * @method _handleApiError
   * @param  {String}        apiName æŽ¥å£å
   * @param  {Object}        res     å‡ºå‚
   * @return {Object}                å¤„ç†è¿‡çš„ res
   */
  function _handleApiError(apiName, res) {
    //é”™è¯¯ç å¼ºåˆ¶è½¬æˆæ•°å­—
    if (__hasOwnProperty(res, 'error')) {
      res.error = parseInt(res.error, 10);
    }
    //å¤„ç† success
    if (_getApiExtra(apiName, 'handleResultSuccess') !== false) {
      _handleResultSuccess(res);
    }
    //å¤„ç† error: 0 çš„æƒ…å†µï¼Œerror ä¸º 0 è¡¨ç¤ºæˆåŠŸ
    if (res.error === 0) {
      delete res.error;
      delete res.errorMessage;
    }

    //æœ‰äº› error ä¸ä»£è¡¨æŽ¥å£å¼‚å¸¸ï¼Œè€Œæ˜¯ç”¨æˆ·å–æ¶ˆæ“ä½œï¼Œä¸åº”è¯¥ç»Ÿä¸€åšæŠ¥é”™æ—¥å¿—ã€‚
    if (res.error > 0 && res.error < 10) {
      console.error(apiName, res);
    }
    return res;
  }
  /**
   * å¤„ç†ç»“æžœä¸­çš„ success å­—æ®µ
   * @method _handleResultSuccess
   * @param  {Object}             res           æŽ¥å£è¿”å›žå€¼
   * @param  {Number}             errorCode     å¯¹åº”é”™è¯¯ç 
   * @param  {Any}                successValue  successå­—æ®µå¤„ç†å€¼
   * @return {Object}                           å¤„ç†åŽçš„ result
   */
  function _handleResultSuccess(res, mappingError, successValue) {
    successValue = __isUndefined(successValue) ? false : successValue;
    if (!__hasOwnProperty(res, 'error') && res.success === successValue) {
      res.error = __isNumber(mappingError) ? mappingError : 2; //2 æ˜¯å‚æ•°é”™è¯¯
    }
    delete res.success;
    return res;
  }

  //å–åˆ° data
  function _handleEventData(evtObj) {
    var data = {};
    if (!__isUndefined(evtObj.data)) {
      data = evtObj.data;
      data = __isObject(data) ? data : { data: data };
    }
    return data;
  }

  /**
   * æ‹†åˆ†ç±»åž‹é”®åé‡ŒçœŸæ­£çš„ key å’Œå¯¹åº”çš„ type
   * @method _separateTypeKey
   * @param  {String}         key å¸¦ç±»åž‹æ ‡è¯†çš„é”®å
   * @return {Object}             è¿”å›žé”®åå’Œç±»åž‹æ ‡è¯†ä¸¤ä¸ªå­—æ®µï¼Œ
   *                              å¦‚{k: 'content', t: '%s'}
   */
  function _separateTypeKey(key) {
    var matches = (key || '').match(/(\w+)(%\w)$/i);
    var tk = {
      k: key
    };
    if (matches) {
      tk.k = matches[1];
      tk.t = matches[2];
    }
    return tk;
  }

  /**
   * æŠŠå€¼è½¬æ¢æˆç›¸åº”ç±»åž‹
   * @method _toType
   * @param  {String} type  ç±»åž‹æ ‡è¯†ï¼Œç›®å‰æ”¯æŒ
   *                        %s(å­—ç¬¦ä¸²)
   *                        %c(16è½¬10è¿›åˆ¶é¢œè‰²)
   *                        %h(10è½¬16è¿›åˆ¶é¢œè‰²)
   *                        %b(ç§»é™¤ base64 æ•°æ®æ ¼å¼å¤´)
   *                        %a{mimeType}(æ·»åŠ  base64 æ•°æ®å¤´)
   *                        %d(æ•´æ•°)
   *                        %f(æµ®ç‚¹æ•°)
   * @param  {any} value å¾…è½¬æ¢å€¼ï¼Œç±»åž‹æœªçŸ¥
   * @return {any}       è½¬æ¢å¥½çš„ç›¸åº”ç±»åž‹çš„
   */
  function _toType(type, value) {
    if (type === '%s') value = __superToString(value);
    if (type === '%c') value = __h2dColor(value);
    //if (type === '%h') value = __d2hColor(value);
    if (type === '%b') value = __removeBase64Head(value);
    if (type === '%d') value = parseInt(value, 10);
    if (type === '%f') value = parseFloat(value);
    return value;
  }
  /**
   * å¤„ç†å¯¹è±¡æ˜ å°„å…³ç³»
   * @method _mapping
   * @param  {Object}  tObj åŽŸå§‹ç›®æ ‡å¯¹è±¡
   * @param  {Object}  map æ˜ å°„å…³ç³»ï¼Œå¦‚{content: 'text'}ï¼Œ
   *                       å³æŠŠ sObj.content çš„å€¼èµ‹ç»™ tObj.textï¼Œ
   *                       å¹¶åˆ é™¤ tObj çš„ content å±žæ€§ï¼Œ
   *                       æ‰€ä»¥ content å°±æ˜¯ sKeyï¼Œtext å°±æ˜¯ tKeyã€‚
   *                       å¯ä»¥æŠŠ map å¯¹è±¡ä¸­çš„å†’å·(:)ç†è§£æˆ toï¼Œ
   *                       å³ {content to text}ã€‚
   *                       å…¶ä¸­ tKey çš„å€¼çš„æœ€åŽå¯ä»¥åŠ  %s ç­‰ç±»åž‹æ ‡è¯†è½¬æ¢æˆç›¸åº”ç±»åž‹ï¼Œ
   *                       æ³¨æ„ï¼šè¦åŠ åˆ°æœ€åŽèµ‹å€¼ç»™ tObj çš„é‚£ä¸ª tKey çš„åŽé¢ã€‚
   *                       è¿™ä¹ˆåšæ˜¯å› ä¸ºï¼š
   *                       æœ‰äº›æŽ¥å£çš„å…¥å‚å­—æ®µç›´æŽ¥ä¼ å…¥éžå­—ç¬¦ä¸²å€¼æ—¶ï¼ŒæŽ¥å£å®Œå…¨æ— å“åº”ï¼Œ
   *                       æ¯”å¦‚ AlipayJSBridge.call('alert',{message: 12345})
   *
   * @param  {Object} sObj å‚ç…§æ¥æºå¯¹è±¡
   * @return {Object}     å¤„ç†æ˜ å°„åŽçš„ tObj
   */
  function _mapping(tObj, map, sObj) {
    var typeKey;
    sObj = sObj || {};
    __forEach(map, function (sKey, tKey) {
      typeKey = _separateTypeKey(map[sKey]);
      //ç›®æ ‡ key
      tKey = typeKey.k;
      //æ˜ å°„æ¡ä»¶ï¼Œå¦åˆ™ä¸èµ‹å€¼ï¼Œé¿å…æ·»åŠ  value ä¸º undefined çš„ key
      if (!__isUndefined(tKey) //ç›®æ ‡ key å®šä¹‰è¿‡
      && (__hasOwnProperty(tObj, sKey) || __hasOwnProperty(sObj, sKey)) //æºæ•°æ®è‡³å°‘æœ‰ä¸€ä¸ªæœ‰æ•ˆ
      && __isUndefined(tObj[tKey]) //ç›®æ ‡æ•°æ®ç©ºç¼ºå¾…èµ‹å€¼
      ) {
          //sKey æ—¢å¯ä»¥æ˜¯ sObj çš„ï¼Œä¹Ÿå¯ä»¥æ˜¯ tObj è‡ªå·±çš„ï¼Œä½†sObj ä¼˜å…ˆçº§é«˜äºŽåŽŸå§‹ tObj
          //å³ sObj[sKey]çš„å€¼ ä¼šè¦†ç›– tObj[sKey]çš„å€¼
          //å¹¶ä¸”è¦æ ¹æ® type å ä½ç¬¦åšç›¸åº”ç±»åž‹è½¬æ¢
          tObj[tKey] = _toType(typeKey.t, __isUndefined(sObj[sKey]) ? tObj[sKey] : sObj[sKey]);
          // åˆ é™¤åŽŸå§‹ tObj ä¸­çš„ sKeyï¼ŒtKey å’Œ sKey åŒåæ—¶ä¸åšåˆ é™¤
          if (tKey !== sKey) {
            delete tObj[sKey];
          }
        }
    });
    return tObj;
  }
  /**
   * ap æŽ¥å£åŸ‹ç‚¹
   * ä¿è¯é˜Ÿåˆ—é‡Œæœ‰è°ƒç”¨è®°å½•æ—¶æ‰å¯åŠ¨è®¡æ—¶å™¨ï¼Œåšåˆ°ä¸è°ƒç”¨ä¸è®¡æ—¶
   * @param {String} apiName  æŽ¥å£å
   */
  var _apiRemoteLog = function () {
    var apiInvokeQueue = [];
    var timerId = void 0;
    var isTimerActived = false;
    //å‘é€æ—¥å¿—
    function triggerSendLog() {
      setTimeout(function () {
        if (apiInvokeQueue.length > 0) {
          var param1 = apiInvokeQueue.join('|');
          AP.ready(function () {
            _JS_BRIDGE.call('remoteLog', {
              type: 'monitor',
              bizType: 'ALIPAYJSAPI',
              logLevel: 1, // 1 - high, 2 - medium, 3 - low
              actionId: 'MonitorReport',
              seedId: 'ALIPAYJSAPI_INVOKE_COUNTER',
              param1: param1
            });
          });
          AP.debug && console.info('REMOTE_LOG_QUEUE>', apiInvokeQueue);
          apiInvokeQueue = [];
        }
        // åœæ­¢è®¡æ—¶å™¨
        clearTimer();
      }, 0);
    }
    // è®¡æ—¶å™¨
    function timer() {
      // è®¡æ—¶æ¿€æ´»æ ‡è‡´
      isTimerActived = true;
      // å¯åŠ¨è®¡æ—¶å™¨
      timerId = setTimeout(function () {
        // æ—¥å¿—å‘é€
        triggerSendLog();
      }, 5000); // 5 ç§’ä¸ŠæŠ¥
    }
    // æ¸…é™¤è®¡æ—¶å™¨
    function clearTimer() {
      !__isUndefined(timerId) && clearTimeout(timerId);
      isTimerActived = false;
    }
    // back äº‹ä»¶ä¸ŠæŠ¥æ—¥å¿—ï¼Œä½œä¸ºå…œåº•
    AP.on('back', function () {
      triggerSendLog();
    });

    return function (apiName) {
      apiInvokeQueue.push(apiName);
      // 6 ä¸ªä¸ŠæŠ¥
      if (apiInvokeQueue.length >= 6) {
        triggerSendLog();
      } else if (!isTimerActived) {
        timer();
      }
    };
  }();

  function _apiLog() {
    var args = __argumentsToArg(arguments);
    var apiName;
    var opt;
    var _opt;
    var res;
    var _res;
    var logs;
    if (AP.debug) {
      apiName = args[0];
      opt = args[1];
      _opt = args[2];
      res = args[3];
      _res = args[4];
      logs = [args.length > 3 ? 'RETURN>' : 'INVOKE>', apiName, __hasOwnProperty(opt, '_') ? opt._ : opt, _opt];
      if (args.length > 3) {
        logs.push(res);
      }
      if (args.length > 4) {
        logs.push(_res);
      }
      console.info(logs);
    }
  }
  /****************** Utilæ–¹æ³• __ ***********************/
  /**
   * æ˜¯å¦åœ¨ UA ä¸­åŒ…å«æŸä¸ªå­—ç¬¦ä¸²
   * @method _inUA
   * @param  {String}   keyStr      ç›®æ ‡å­—ç¬¦ä¸²
   * @return {Boolean}              æ˜¯å¦åŒ…å«
   */
  function __inUA(keyPattern) {
    return keyPattern.test(_UA);
  }
  /**
   * åŠ¨ç”»å¸§
   * @method raf
   * @param  {Function} fn å›žè°ƒ
   * @return {Function}    requestAnimationFrame
   */
  var __raf = function () {
    return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.msRequestAnimationFrame || function (callback, element) {
      window.setTimeout(function () {
        callback(+new Date(), element);
      }, 1000 / 60);
    };
  }();
  /**
   *  å½“å‰çŽ¯å¢ƒæ˜¯å¦æ”¯æŒ Promise
   * @method __supportPromise
   * @return {Boolean}              æ˜¯å¦æ”¯æŒ
   */
  function __isSupportPromise() {
    if (_IS_SUPPORT_PROMISE === undefined) {
      var isSupport = false;
      var P = self.Promise;

      if (P) {
        var promise = null;
        var then = null;
        try {
          promise = P.resolve();
          then = promise.then;
        } catch (e) {
          // silently ignored
        }
        if (promise instanceof P && __isFunction(then) && !P.cast) {
          isSupport = true;
        }
      }
      if (!isSupport) {
        console.warn('try callback since no Promise detected');
      }
      _IS_SUPPORT_PROMISE = isSupport;
    }
    return _IS_SUPPORT_PROMISE;
  }

  /**
   * è¶…çº§å­—ç¬¦ä¸²è½¬æ¢
   * @method __superToString
   * @param  {Any}        content å¾…è½¬æ¢å†…å®¹
   * @return {String}             è½¬æ¢åŽçš„å­—ç¬¦ä¸²
   */
  function __superToString(content) {
    var str = content;
    if (__isObject(content) || __isArray(content)) {
      try {
        str = JSON.stringify(content);
      } catch (e) {
        //é™é»˜
      }
    } else {
      str = content + '';
    }
    return str;
  }

  /**
   * 16è¿›åˆ¶é¢œè‰²è½¬æˆ10è¿›åˆ¶æ•°å­—
   * @method __h2dColor
   * @param  {String}   hex 16è¿›åˆ¶é¢œè‰²å­—ç¬¦ä¸²
   * @return {Number}       10è¿›åˆ¶æ•°å­—
   */
  function __h2dColor(hex) {
    var dec = '' + hex;
    //å¦‚æžœåŠ äº†#å·ï¼ŒåŽ»æŽ‰
    if (dec.indexOf('#') === 0) {
      dec = dec.substr(1);
    }
    //å¦‚æžœæ˜¯3ä½ç®€å†™ï¼Œè¡¥å…¨æˆ6ä½
    if (dec.length === 3) {
      dec = dec.replace(/(.)/g, '$1$1');
    }
    dec = parseInt(dec, 16);
    if (__isNaN(dec)) {
      console.error(hex + ' is invalid hex color.');
    }
    return dec;
  }

  /**
   * 10è¿›åˆ¶æ•°å­—è½¬æˆ16è¿›åˆ¶é¢œè‰²
   * @method __d2hColor
   * @param  {Number}   dec 10è¿›åˆ¶æ•°å­—
   * @return {String}       16è¿›åˆ¶é¢œè‰²å­—ç¬¦ä¸²
   */
  // function __d2hColor(dec) {
  //   return '#' + dec.toString(16);
  // }
  /**
   * native è¿”å›žçš„æ— å¤´ base64 æ•°æ®ï¼Œæ·»åŠ æµè§ˆå™¨è¯†åˆ«çš„ mimeType çš„ base64æ•°æ®å¤´
   * @method __addBase64Head
   * @param   {String}        base64   æ— å¤´æ•°æ®
   * @param   {String}        mimeType æ•°æ®æ ¼å¼
   * @return  {String}                 æœ‰å¤´æ•°æ®
   */
  function __addBase64Head(base64, mimeType) {
    if (base64 && mimeType) {
      base64 = 'data:' + mimeType + ';base64,' + base64;
    }
    return base64;
  }

  /**
   * ç§»é™¤ base64 æ•°æ®å¤´ï¼Œnative æŽ¥å£ä¸éœ€è¦ä¼ å…¥å¤´éƒ¨
   * @method __removeBase64Head
   * @param  {String}           base64 æœ‰å¤´æ•°æ®
   * @return {String}                  æ— å¤´æ•°æ®
   */
  function __removeBase64Head(base64) {
    if (__isString(base64)) {
      base64 = base64.replace(/^data:(\/|\w|\-|\.)+;base64,/i, '');
    }
    return base64;
  }

  /**
   * æŠŠ json è½¬æˆ & ç›¸è¿žçš„è¯·æ±‚å‚æ•°
   * @method __toQueryString
   * @param  {Object}        data key: valueå‚æ•°é”®å€¼å¯¹
   * @return {String}             queryString
   */
  function __toQueryString(data) {
    var result = [];

    __forEach(data, function (key, value) {
      result.push(key + '=' + encodeURIComponent(__isUndefined(value) ? '' : value));
    });
    result = result.join('&');
    // var limits = [1024, 2048];
    // var notice;
    // notice = 'query string length has more than %dï¼Œplease use setSessionData interface';
    // if (result.length > limits[1]) {
    //   console.warn(notice, limits[1]);
    // } else if (result.length > limits[0]) {
    //   console.warn(notice, limits[0]);
    // }
    return result;
  }

  /**
   * æž„é€ å¸¦å‚çš„å®Œæ•´ url
   * @method __buildUrl
   * @param  {String}   url    åŽŸå§‹ urlï¼Œå¯èƒ½å·²ç»æœ‰ queryString
   * @param  {Object}   params url å‚æ•°å¯¹è±¡
   * @return {String}          æ‹¼æŽ¥å¥½çš„å¸¦å‚ url
   */
  function __buildUrl(url, params) {
    var qs = params;
    if (__isObject(params)) {
      qs = __toQueryString(params);
    }
    if (!/\?/.test(url)) {
      qs = '?' + qs;
    } else if (!/&$/.test(url) && !/\?$/.test(url)) {
      qs = '&' + qs;
    }
    return url + qs;
  }
  /**
   * ä¸€ä¸ªå¯¹è±¡æ˜¯å¦å«æœ‰æŸä¸ª key
   * @method __hasOwnProperty
   * @param  {Object}         obj å¯¹è±¡æˆ–æ•°ç»„
   * @param  {String}         key é”®å€¼
   * @return {Boolean}            æ˜¯å¦å«æœ‰æ­¤é”®å€¼
   */
  function __hasOwnProperty(obj, key) {
    if (__isObject(obj) || __isArray(obj)) {
      return obj.hasOwnProperty(key);
    }
    return false;
  }
  /**
   * éåŽ†å¯¹è±¡
   * @method __forEach
   * @param  {Object}   obj å¾…éåŽ†å¯¹è±¡æˆ–æ•°ç»„
   * @param  {Function} cb  æ¯ä¸ª key çš„å›žè°ƒ
   *                        å›žè°ƒå…¥å‚æ˜¯ key å’Œå¯¹åº”çš„ value
   */
  function __forEach(obj, cb, notArray) {
    var i;
    var key;
    if (!notArray && __likeArray(obj)) {
      for (i = 0; i < obj.length; i++) {
        if (cb(i, obj[i]) === false) {
          return obj;
        }
      }
    } else {
      for (key in obj) {
        if (cb(key, obj[key]) === false) {
          return obj;
        }
      }
    }
    return obj;
  }
  /**
   * è§£æž JSON
   * @method __parseJSON
   * @param  {String}    str JSON å­—ç¬¦ä¸²
   * @return {Object}        JSON å¯¹è±¡
   */
  function __parseJSON(str) {
    try {
      str = JSON.parse(str);
    } catch (err) {
      console.warn(err, str);
    }
    return str;
  }

  /**
   * è½¬æˆå°å†™å­—æ¯
   * @method __tlc
   * @param  {String} str å¾…è½¬æ¢å­—ç¬¦ä¸²
   * @return {String}     å°å†™å­—ç¬¦ä¸²
   */
  function __tlc(str) {
    if (__isString(str)) {
      str = str.toLowerCase();
    }
    return str;
  }

  /**
   * è½¬æˆå¤§å†™å­—æ¯
   * @method __tuc
   * @param  {String} str å¾…è½¬æ¢å­—ç¬¦ä¸²
   * @return {String}     å¤§å†™å­—ç¬¦ä¸²
   */
  function __tuc(str) {
    if (__isString(str)) {
      str = str.toUpperCase();
    }
    return str;
  }

  function __isAndroid() {
    return __inUA(/android/i);
  }

  function __isIOS() {
    return __inUA(/iPad|iPod|iPhone|iOS/i);
  }

  function __isUndefined(o) {
    return __type_original(o) === '[object Undefined]';
  }

  function __isNull(o) {
    return __type_original(o) === '[object Null]';
  }

  function __isNaN(num) {
    return parseInt(num, 10).toString() === 'NaN';
  }
  function __isBoolean(val) {
    return typeof val === 'boolean';
  }

  function __isFunction(fn) {
    return __type_original(fn) === '[object Function]';
  }

  function __isString(str) {
    return typeof str === 'string';
  }

  function __isObject(o) {
    return __type_original(o) === '[object Object]';
  }

  function __isNumber(num) {
    // å¦‚æžœç”¨typeof number ä¼šç”ŸæˆSymbolPolyfill
    return __type_original(num) === '[object Number]';
  }

  function __isArray(arr) {
    return __type_original(arr) === '[object Array]';
  }

  function __likeArray(obj) {
    return !!obj && !__isFunction(obj) && (__isArray(obj) || __isNumber(obj.length));
  }
  function __isEvent(evt) {
    return __type_original(evt) === '[object Event]';
  }

  function __type_original(obj) {
    return Object.prototype.toString.call(obj);
  }

  function __isEmptyObject(obj) {
    for (var name in obj) {
      return false;
    }
    return true;
  }

  function __argumentsToArg(_arguments) {
    var _startIndex = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 0;

    var len = _arguments.length - _startIndex;
    var arg = new Array(len);
    for (var i = 0; i < len; i++) {
      arg[i] = _arguments[i + _startIndex];
    }
    return arg;
  }

  /**
   * å¯¹è±¡æ‰©å±•
   * @method __extend
   * @param  {Object} obj  åŽŸå§‹å¯¹è±¡
   * @param  {Object} args å¤šä¸ªç»§æ‰¿å¯¹è±¡
   * @return {Object}      æ‰©å±•åŽå¯¹è±¡
   */
  function __extend(obj) {
    var args = __argumentsToArg(arguments, 1);
    var source;
    var prop;
    if (!__isObject(obj)) {
      return obj;
    }
    for (var i = 0, length = args.length; i < length; i++) {
      source = args[i];
      for (prop in source) {
        if (hasOwnProperty.call(source, prop)) {
          obj[prop] = source[prop];
        }
      }
    }
    return obj;
  }

  /***************** è¾“å‡º AP å¯¹è±¡ *******************/
  self._AP = AP;

  if (typeof module !== 'undefined' && module.exports) {
    // å…¼å®¹ CommonJS
    module.exports = AP;
  } else if (typeof define === 'function' && (define.amd || define.cmd)) {
    // å…¼å®¹ AMD / RequireJS / seaJS
    define(function () {
      return AP;
    });
  } else {
    // å¦‚æžœä¸ä½¿ç”¨æ¨¡å—åŠ è½½å™¨åˆ™è‡ªåŠ¨ç”Ÿæˆå…¨å±€å˜é‡
    self.ap = self.AP = AP;
  }
})(self);