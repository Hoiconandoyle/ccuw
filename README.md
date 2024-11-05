# Mail Server Project
This project aims to implement the basic functionalities of a mail server, including the manipulation of emails, attachments, and contacts. The server provides a web user interface for easy access and management of emails. It was developed using Java Spring Boot for the server-side and Vue.js for the client-side.
**for full report**: [Report.pdf](https://github.com/mahfouz72/mail-service/files/13801779/Report.pdf)

### Features:
- Inbox Folder: Displays emails from newest to oldest by default.
- Composing and Drafts: Users can compose new emails and send them to any number of receivers or save them as draft.
- User Folders: Users can create, rename, and delete their own folders.
- Filters: Emails can be filtered based on different criteria
- Attachments: Users can add, delete, and view attachments in emails.
- sorting: emails in any folder can be sorted based on date or priority in ascending or descending order
- Bulk Operations: Users can perform bulk operations on selected emails (move, delete)

### Installation
1. Clone the repository
2. Open the server-side project in Java Spring Boot IDE.
3. Build and run the server-side application.
4. Open the client-side project in a Vue.js IDE.
5. Install dependencies: npm install
6. Start the client-side application: npm run serve
7. Access the application in your browser at http://localhost:8080

### Design Patterns

1. Facade  pattern:
   - The Façade pattern was used to simplify access to the set of objects related to email handling (such as UserService, DraftService, AttachmentService). The MailSenderFacade provides a simplified interface for     clients to communicate with these objects, hiding the complexity of the underlying subsystem components.
   
2. Filter Pattern:
   - The Filter pattern was used to implement the ability to filter mails based on different criteria. The Filter interface defines a common method for filtering mails, and concrete filter classes (e.g.,               SenderFilter, ReceiverFilter) implement this interface to provide specific filtering logic. Multiple filters can be chained together to create complex filtering conditions.
  
3. Builder Pattern:
   - The Builder pattern was employed to construct complex Mail objects step by step. The MailBuilder class provides a fluent interface to set different attributes of the Mail object, allowing for selective           modification of specific attributes. This pattern is particularly useful when dealing with objects that have a large number of optional parameters or configuration settings.

4. Chain of Responsibility Pattern:
   - The Chain of Responsibility pattern was used to process authentication-related requests. The base Handler class defines a common interface for concrete handlers, and each handler in the chain decides whether     to process the request or pass it to the next handler in the chain. This pattern provides a flexible and modular way to handle authentication requests.

5. Singlton Pattern

### Full UML diagram
![all](https://github.com/mahfouz72/mail-service/assets/125591358/d61cf25b-588b-4155-bc1a-5b69d2b0efec)

### Screenshots
![image 5](https://github.com/mahfouz72/mail-service/assets/125591358/238de1d1-028b-4e0b-b7cf-3279a1bf8816)
![image 1](https://github.com/mahfouz72/mail-service/assets/125591358/7b993e85-ace2-4348-aeee-9c6b04b1ad16)
![image 2](https://github.com/mahfouz72/mail-service/assets/125591358/1d07a766-7712-43e4-a226-4d2cd512715d)
![image 4](https://github.com/mahfouz72/mail-service/assets/125591358/c7e9a4a2-e1cb-48e3-9946-9e2108997996)
![image 3](https://github.com/mahfouz72/mail-service/assets/125591358/a543d610-f007-48fb-a253-6300cd465e4e)

### Contributors
- <a href="https://github.com/mahfouz72">Mohamad Mahfouz</a>
- <a href="https://github.com/esmailMahmouds">Esmail Mahmoud</a>
- <a href="https://github.com/YoussifKhaled">Youssif Khaled</a>
- <a href="https://github.com/Aly-El-Din">Aly ElDin</a>
