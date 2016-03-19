# local development #


## admin interface ##

there is an admin interface: http://localhost:8888/_ah/admin - here its possible to browse the data storage TTW

## logging ##

- in logging.properties set .level = FINE
at least under development - not sure if its possible to have 2 different properties setups

development: .level = WARNING
development: .level = FINE
deployment:  .level = INFO


### print request ###

Based on the GuestBook tutorial - we had to inspect a post from a Android App.

```
    
public class SignGuestBookServlet extends HttpServlet {
    private static final Logger log =
            Logger.getLogger(SignGuestBookServlet.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        log(req.toString());

    ...
    
```

results in:

```

    INFO: javax.servlet.ServletContext log: sign: POST /sign HTTP/1.1
    Content-Length: 60
    Content-Type: application/x-www-form-urlencoded
    Host: 10.0.1.7:8888
    Connection: Keep-Alive
    User-Agent: Apache-HttpClient/UNAVAILABLE (java 1.4)
    Expect: 100-continue

```


## run development server on ip address ##

set argument in the run path

```
    
    --address=10.0.1.7 
    
```

screen shoot:

http://screencast.com/t/NZEdpjkuUk







## make/set index's ##

todo


## make/set index's ##

set threads


