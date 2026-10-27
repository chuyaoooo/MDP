# Student user stories

Must = committed for Part 2; Nice = if time allows.  
Criteria assume a signed-in student.  
“Code” = the rotating QR and typed code shown by the club (refreshes every 10 seconds).

| ID | Story | Acceptance criteria | Priority | Req |
|----|-------|---------------------|----------|-----|
| S-01 | As a student, I want to sign in with my university account so that only I can access my bookings and points. | Valid credentials → Explore screen. Attempt to open club screens or another student’s data → access denied. | Must | R6 |
| S-02 | As a student, I want to browse events by day and filter by category or club so that I can quickly find relevant events. | Approved events are shown with remaining places and points value. | Must | R2 |
| S-03 | As a student, I want to book a place without double-booking so that I never commit to two events at the same time. | Free place → booking appears in My Events. Time overlap → booking blocked and the clashing event is named. | Must | R2 |
| S-04 | As a student, I want a waitlist for full events and the ability to cancel so that I still have a chance to attend and can free my place. | Full event → join waitlist; place becomes free → invitation received. Cancel allowed only if the event is at least 2 days away. | Must | R2 |
| S-05 | As a student, I want to check in by scanning the QR or typing the code so that I am counted quickly in a crowded room. | Attendance open + valid current code → “Checked in”. | Must | R3 |
| S-06 | As a student, I want expired or already-used codes to be rejected so that people who were not present cannot earn points. | Code older than 10 seconds, attendance closed, or already checked in → rejected. | Must | R3 |
| S-07 | As a student, I want to check in while offline and see the sync status so that poor signal does not cost me points. | No connection → saved as Pending. After reconnect → Synced, Verified, or Rejected with reason. | Must | R4 |
| S-08 | As a student, I want forged or repeated records to be rejected so that cheating cannot devalue genuine points. | Duplicate or tampered upload → credited at most once or rejected. | Must | R3, R4 |
| S-09 | As a student, I want to see my points and event history so that I know my current S-CPD standing. | Successful verification → points credited immediately and shown as Credited. Until verification → Pending. | Must | R5 |
| S-10 | As a student, I want to be notified when points are removed and why so that I understand the decision. | Admin invalidates a record → total decreases and notification with reason is received. | Must | R5 |
| S-11 | As a student without a booking, I want to check in when the club allows walk-ins so that last-minute attendance can still count. | Walk-ins allowed + valid code → recorded as walk-in. | Nice | R3 |
| S-12 | As a student, I want to book an event while offline so that I do not miss opportunities when my connection drops. | No connection → request saved as Pending. After sync → Confirmed, Waitlisted, or Rejected. | Must | R2, R4 |
| S-13 | As a student, I want smart reminders for my booked events so that I do not forget to attend or miss check-in. | Notifications 1 day before, 1 hour before, and when attendance opens. Each type can be turned off. | Must | F-01 |
| S-14 | As a student, I want to see a points leaderboard so that I am motivated to participate more. | Top 10 students + own rank (credited points only). Opt-out available. | Nice | F-03 |
