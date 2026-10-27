# Student user stories

Must = committed for Part 2; Nice = if time allows. Criteria assume a signed-in student. "Code" = the QR and typed code the club shows, refreshed every 10 s.

All stories are written from a student perspective. 

| ID | Story | Acceptance criteria | Priority | Req |
|-----|-------|---------------------|----------| --- |
| S-01 | I want to sign in with my university account convienience. | Valid credentials → Explore. Club/admin screens or other students’ data → access denied. | Must | R6 |
| S-02 | I want to browse events by day and filter by S-CPD category or club so that I quickly find ones worth attending. | Able to filter to find approved events shown with places left and points. | Must | R2 |
| S-03 | I want to book a place without double-booking to never commit to two events.| Free place → appears in My events. Overlap → blocked with clash named.| Must | R2 |
| S-04 | I want a waitlist for full events, if a slot frees up, I still get a chance to attend. | Full event → join waitlist; place frees → invitation. | Must | R2 |
| S-05 | I want to check in via QR or code that possible in a crowded room. | Attendance open + valid current code → “Checked in”. | Must | R3 |
| S-06 | I want old/expired codes refused so that non-attendees cannot earn points I worked for. | Code >10 s old, closed window, or already checked in → rejected. | Must | R3 |
| S-07 | I want to check in without a connection so that poor signal never costs me points. | No connection → saved as Pending. Sync → Synced / Rejected with reason. | Must | R4 |
| S-08 | I want forged or repeated records rejected for fairness. | Duplicate or tampered upload → credited at most once or rejected. | Must | R3, R4 |
| S-09 | I want to see my points and event history to know my record. | Immediate credit on successful Cloud Functions verification, with admin able to reverse later. Event history shown in profile. | Must | R5 |
| S-10 | I want to be told when points are removed, and why, so that I understand and can respond. | Admin invalidates → total drops + reason notification. | Must | R5 |
| S-11 | If without a booking, I want to check in when the club allows walk-ins so that last-minute attendance still counts. | Walk-ins allowed + valid code → recorded as walk-in. | Nice | R3 |
| S-12 | I want to book an event while offline so that I don't miss an opportunity when my connection drops. | No connection → Pending locally. Sync → Confirmed / Waitlisted / Rejected. | Must | R2, R4 |
| S-13 | I want to receive reminders for my booked events so that I do not forget to attend. | 1 day, 1 hour before event → notification. Each type toggle-able. | Must | Own (F-01) |
| S-14 | I want to see a points leaderboard so that I am motivated to participate more. | Top 10 + own rank (credited points only). Opt-out available. | Nice | Own (F-03) |
