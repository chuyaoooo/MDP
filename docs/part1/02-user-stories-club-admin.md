# Club and admin user stories

Must = committed for Part 2; Nice = if time allows. "Club organiser" is the Club actor. The S-CPD admin works in the backend console, so A-stories have no app screens. Criteria assume a signed-in user with the right role.

## Club organiser

| ID | Story | Acceptance criteria | Priority | Req |
|----|-------|---------------------|----------|-----|
| C-01 | As a club organiser, I want to sign in with my university account so that only my club's organisers manage its events. | Given a club account, when I sign in, then I see the club dashboard.<br>Given I approve my own event, edit another club's event or open student screens, then access is denied. | Must | R6 |
| C-02 | As a club organiser, I want to create an event and request S-CPD points so that students who want those points attend. | Given valid title, description, date and time, venue, capacity and points requested, when I submit, then the event is Pending approval. | Must | R1, R5 |
| C-03 | As a club organiser, I want to see each event's status and the admin's reason so that I know what to do next. | Given my events, then each shows Pending approval, Approved, Rejected, Completed or Cancelled.<br>Given a Rejected event, when I open it, then I see the reason and can submit a new request. | Must | R1 |
| C-04 | As a club organiser, I want approved events locked so that students can rely on the details I published. | Given an Approved event, when I try to change any detail, then the fields are read-only and the backend rejects the edit. | Must | R1 |
| C-05 | As a club organiser, I want to open and close attendance and show a rotating QR and code so that only people in the room can check in. | Given an Approved event on its day, when I tap Start attendance, then the QR and code refresh every 10 s and check-ins are accepted; after Close, they are rejected (server time). | Must | R3 |
| C-06 | As a club organiser, I want to see sign-ups and check-ins so that I can spot problems on the day and report attendance afterwards. | Given an Approved event, then I see the sign-up rate and attendee list, with check-ins appearing within seconds and, after closing, attended and no-show counts. | Must | R3 |
| C-07 | As a club organiser, I want to request cancellation of an approved event with a reason so that students are told properly when plans change. | Given an Approved event, when I submit a reason and the S-CPD admin approves, then it is Cancelled and booked and waitlisted students are notified with the reason. | Nice | R1 |

## S-CPD admin (backend)

| ID | Story | Acceptance criteria | Priority | Req |
|----|-------|---------------------|----------|-----|
| A-01 | As an S-CPD admin, I want to approve or reject event requests and confirm their points so that only suitable events reach students, at consistent values. | Given a Pending request, when I set Approved with a confirmed points value, then the event appears in the marketplace; when I set Rejected with a reason, the club sees it. | Must | R1, R5 |
| A-02 | As an S-CPD admin, I want to approve an event's attendance after it ends so that points go only to genuine attendees. | Given the event has ended, when I approve the attendance list, then each validated attendee is credited the confirmed points; before then, none are. | Must | R5 |
| A-03 | As an S-CPD admin, I want to invalidate a record that fails the criteria so that nobody keeps points unfairly. | Given a suspect record, even after crediting, when I mark it Invalid with a reason, then a reversal entry is added to the ledger and the student is notified. | Must | R5 |
| A-04 | As an S-CPD admin, I want roles to come from school records so that nobody can give themselves club or admin rights. | Given the school roster, when a user signs in, then their role comes from backend records; a request to change one's own role is rejected. | Must | R6 |
