# Club and admin user stories

Must = committed for Part 2; Nice = if time allows.  
Admin works only in the backend (no app screens).

## Club organiser

| ID | Story | Acceptance criteria | Priority | Req |
|----|-------|---------------------|----------|-----|
| C-01 | As a club organiser, I want to sign in with my university account so that only authorised organisers can manage the club’s events. | Club account → club dashboard. Attempts to approve own events, edit other clubs, or open student screens → denied. | Must | R6 |
| C-02 | As a club organiser, I want to create an event and request S-CPD points so that students can discover and attend it. | Valid title, description, date/time, venue, capacity and requested points submitted → status becomes Pending approval. | Must | R1, R5 |
| C-03 | As a club organiser, I want to see each event’s status and any rejection reason so that I know what to do next. | Status shown as Pending, Approved, Rejected, Completed or Cancelled. Rejected events show the reason and allow a new submission. | Must | R1 |
| C-04 | As a club organiser, I want approved events to be locked so that published details remain reliable for students. | Any edit attempt on an Approved event → fields read-only and backend rejects the change. | Must | R1 |
| C-05 | As a club organiser, I want to open and close attendance and display a rotating QR and code so that only people present can check in. | Start attendance → QR and code refresh every 10 seconds (server time). Close attendance → further check-ins rejected. | Must | R3 |
| C-06 | As a club organiser, I want to see sign-ups and live check-ins so that I can manage the event on the day. | Sign-up rate and attendee list visible; check-ins appear within seconds; final attended and no-show counts shown after closing. | Must | R3 |
| C-07 | As a club organiser, I want to request cancellation of an approved event with a reason so that students are properly informed. | Cancellation request + reason → admin approval → event Cancelled and booked/waitlisted students notified. | Nice | R1 |
| C-08 | As a club organiser, I want to allow or disallow walk-ins during the event so that empty seats can be filled without exceeding capacity. | Walk-ins Allowed → unbooked students can check in. Walk-ins Disallowed → unbooked check-in attempts rejected. | Nice | R3 |

## S-CPD admin (backend)

| ID | Story | Acceptance criteria | Priority | Req |
|----|-------|---------------------|----------|-----|
| A-01 | As an S-CPD admin, I want to approve or reject event requests and confirm the points value so that only suitable events appear to students. | Pending request → Approved with confirmed points (appears in marketplace) or Rejected with reason (visible to club). | Must | R1, R5 |
| A-02 | As an S-CPD admin, I want to invalidate an attendance record that fails the criteria so that points are not kept unfairly. | Suspect record marked Invalid with reason → reversing ledger entry created and student notified. | Must | R5 |
| A-03 | As an S-CPD admin, I want roles to come from school records so that users cannot grant themselves elevated privileges. | Role is assigned by the backend on sign-in. Any attempt to change one’s own role → rejected. | Must | R6 |
