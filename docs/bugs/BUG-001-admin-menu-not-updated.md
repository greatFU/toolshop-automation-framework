# BUG-001: Admin menu is not displayed after successful login

## Preconditions

The admin user is logged out.

## Steps

1. Open the Toolshop application.
2. Click Sign in.
3. Enter valid admin credentials.
4. Click Login.
5. Observe the navigation bar.

## Actual result

The admin dashboard opens successfully, but the navigation bar
continues to display "Sign in".

## Expected result

The navigation bar should display the authenticated admin user menu.

## Additional information

The issue is reproducible manually in Incognito mode.

JavaScript errors are also displayed in the browser console:

TypeError: Cannot read properties of null (reading 'toFixed')

## Evidence

- Screenshot of the admin dashboard with Sign in still displayed.
- Screenshot of the browser console errors.