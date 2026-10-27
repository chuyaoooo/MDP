# Club and admin user stories

Must = committed for Part 2. Admin works only in backend (no app screens).

## Club organiser

| ID | Story | Acceptance criteria | Priority | Req |
|----|-------|---------------------|----------|-----|
| C-01 | I want to sign in with my university account and manage its events. | Club account → dashboard. Own-event approval, other clubs, or student screens → denied.| Must | R6 |
| C-02 | I want to create an event and request S-CPD points with a straightforward process. | Valid title, description, date and time, venue, capacity and points submitted → Pending approval. | Must | R1, R5 |
| C-03 | I want to see each event's status and the admin's notes to proceed. | Shows Pending / Approved / Rejected / Completed / Cancelled. Rejected → reason + can resubmit new request. | Must | R1 |
| C-04 | I want approved events locked so there is no last minute changes. | Any edit attempt → fields read-only; backend rejects.| Must | R1 |
| C-05 | I want to open and close attendance with a rotating QR and code so that only people in the room can check in. | Start → QR/code refreshes every 10 s (server time). Close → further check-ins rejected. | Must | R3 |
| C-06 | I want to see sign-ups and check-ins prepare for the event. | Sign-up rate + attendee list; check-ins appear in seconds; final attended/no-show counts. | Must | R3 |
| C-07 | I want to request cancellation of an approved event with a valid reason, and sign-ups are notified of it. | Cancel approved event + reason → admin approval → Cancelled + students notified. | Nice | R1 |
| C-08 | I want to toggle walk-ins on or off during my event so that I can fill empty seats.| I set walk-ins to "Allowed"→ unbooked students can check in.<br>Set walk-ins are "Disallowed", reject an unbooked student's attempt to check in. | Nice | R3 |

## S-CPD admin (backend)

| ID | Story | Acceptance criteria | Priority | Req |
|----|-------|---------------------|----------|-----|
| A-01 | I want to approve or reject event requests| With pending request, Approved + points → appears in marketplace. Rejected + reason → club sees it. | Must | R1, R5 |
| A-02 | I want to invalidate a record that fails the criteria| Given a suspect record, Mark Invalid + reason → ledger reversal + student notified. | Must | R5 |
| A-03 | I want roles to come from school records so that nobody can give themselves club or admin rights. | Role assigned by backend on sign-in. Self-role change → rejected. | Must | R6 |
