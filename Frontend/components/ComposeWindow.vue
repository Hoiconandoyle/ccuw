<template>
    <div class="compose">
        <table class="compose">
            <thead>
                <th class="title">New Email</th>
                <th class="close" @click="saveDraft">x</th>
            </thead>
            <tr>
                <input class="To" type="text" id="Input1" v-model="toField" placeholder="To" />
                <select :class="getPriorityColor(priority)" class="Priority" v-model="priority">
                    <option v-for="option in priorityOptions" :value="option" :key="option"
                        :class="getPriorityColor(option)">
                        {{ option }}
                    </option>
                </select>
            </tr>
            <tr>
                <input class="subject" type="text" id="Input2" v-model="subjectField" placeholder="Subject" />
            </tr>
            <tr>
                <textarea class="mailBody" id="textAreaInput" name="textAreaInput" style="resize: none;"
                    v-model="bodyField"></textarea>
            </tr>
            <tr class="downBar">
                <input type="file" id="fileupload" ref="fileInput" @change="uploadfile(e)" style="display: none;">
                <button class="pi pi-paperclip attach" @click="this.$refs.fileInput.click();"></button>
                <div class="attchList">
                    <div v-for="attchmnt in attachments" :key="attchmnt" class="Attachment">
                        <span class="attchName" @click="download(attchmnt)">{{ chopString(attchmnt.fileName, 11) }}</span>
                        <button class="pi pi-times attchRemove" @click="detach(attchmnt)"></button>
                    </div>
                </div>
                <button class="pi pi-send send" @click="sendmail()"></button>
            </tr>
        </table>
    </div>
</template>

<script>
export default {
    name: 'ComposeWindow',
    emits: ['closeComposeWindow'],
    props: ['useremail', 'email', 'windowState'],
    data() {
        return {
            toField: this.windowState === 'viewDraft' ? this.email.to[0] : '',
            subjectField: this.windowState === 'viewDraft' ? this.email.subject : '',
            bodyField: this.windowState === 'viewDraft' ? this.email.body : '',
            attachment: {
                id: '',
                fileName: '',
            },
            attachments: this.windowState === 'viewDraft' ? this.email.attachments : [],
            priority: this.windowState === 'viewDraft' ? this.email.priority.charAt(0).toUpperCase() + this.email.priority.slice(1).toLowerCase() : 'Medium',
            priorityOptions: ['Urgent', 'High', 'Medium', 'Low'],
            wasDraft: this.windowState === 'viewDraft' ? true : false,
        }
    },
    methods: {
        saveDraft() {
            if (this.windowState === 'viewDraft') {
                this.editDraft();
                return;
            }
            this.$emit('closeWindow')
            const mailRequest = {
                from: this.useremail,
                to: this.toField.replace(/\s/g, "").split(","),
                subject: this.subjectField,
                body: this.bodyField,
                priority: this.priority.toUpperCase(),
            };
            let attachmentIds = [];
            for (let i = 0; i < this.attachments.length; i++) {
                console.log(this.attachments[i].id)
                attachmentIds.push(this.attachments[i].id)
            }
            console.log(attachmentIds)
            // Convert the list to a comma-separated string
            attachmentIds = attachmentIds.join(',');
            console.log("mail reuest: " + JSON.stringify(mailRequest))
            fetch('http://localhost:8080/createDraft/' + this.useremail + `?attachmentIds=${attachmentIds}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(mailRequest),
            })
                .then(res => res.json())
                .then(data => {
                    console.log(JSON.stringify(data))
                })
                .catch(error => {
                    console.error('Error:', error)
                });
        },
        editDraft() {
            const mailRequest = {
                from: this.useremail,
                to: this.toField.replace(/\s/g, "").split(","),
                subject: this.subjectField,
                body: this.bodyField,
                priority: this.priority.toUpperCase(),
            };
            let attachmentIds = [];
            for (let i = 0; i < this.attachments.length; i++) {
                console.log(this.attachments[i].id)
                attachmentIds.push(this.attachments[i].id)
            }
            console.log(attachmentIds)
            // Convert the list to a comma-separated string
            attachmentIds = attachmentIds.join(',');
            console.log("mail reuest: " + JSON.stringify(mailRequest))
            fetch('http://localhost:8080/updateDraft/' + this.useremail + '/' + this.email.id + `?attachmentIds=${attachmentIds}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(mailRequest),
            })
                .then(res => res.json())
                .then(data => {
                    console.log(JSON.stringify(data))
                })
                .catch(error => {
                    console.error('Error:', error)
                });
            
            this.$emit('closeWindow')
        },
         sendmail() {
            this.$emit('closeWindow')
            const mailRequest = {
                id: this.windowState === 'viewDraft' ? this.email.id : '',
                from: this.useremail,
                to: this.toField.replace(/\s/g, "").split(","),
                subject: this.subjectField,
                body: this.bodyField,
                priority: this.priority.toUpperCase(),
            };
            let attachmentIds = [];
            for (let i = 0; i < this.attachments.length; i++) {
                console.log(this.attachments[i].id)
                attachmentIds.push(this.attachments[i].id)
            }
            console.log(attachmentIds)
            // Convert the list to a comma-separated string
            attachmentIds = attachmentIds.join(',');
            console.log("mail reuest: " + JSON.stringify(mailRequest))
            console.log("wasDraft: " + this.wasDraft)
            fetch(`http://localhost:8080/compose?wasDraft=${this.wasDraft}&attachmentIds=${attachmentIds}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(mailRequest),
            })
                .then(res => res.json())
                .then(data => {
                    console.log(JSON.stringify(data))
                })
                .catch(error => {
                    console.error('Error:', error)
                });
        },
        async uploadfile() {
            let fileInput = document.getElementById('fileupload');
            let formData = new FormData();
            formData.append("data", fileInput.files[0]);
            await fetch('http://localhost:8080/attach', {
                method: "POST",
                body: formData
            }).then(result => result.json())
                .then(result => {
                    this.attachments.push({ id: result.id, fileName: result.fileName })
                });
            this.$refs.fileInput.value = '';
        },
        download(attachment) {
            const id = attachment.id;
            console.log(attachment)
            if (attachment.contentType == null) {
                fetch('http://localhost:8080/download/' + id, {
                    method: "GET"
                }).then(res => {
                    console.log("downloaded successfully") //testing
                    window.open(res.url)
                })
            }
            else {
                fetch('http://localhost:8080/view/' + this.useremail + '/' + this.email.id + '/' + 'draft' + '/' + id, {
                    method: "GET"
                }).then(res => {
                    console.log("downloaded successfully") //testing
                    window.open(res.url)
                })
            }
        },
        detach(attachment) {
            const id = attachment.id;
            if (attachment.contentType == null) {
                fetch('http://localhost:8080/detach/' + id, {
                    method: "DELETE",
                }).then(res => {
                    for (let i = 0; i < this.attachments.length; i++) {
                        if(this.attachments[i].id === id) {
                            this.attachments.splice(i, 1);
                        }
                    }
                    console.log(res.url) //testing
                })
            }else{
                fetch('http://localhost:8080/deleteAttachment/' + this.useremail + '/' + this.email.id + '/' + id, {
                    method: "DELETE",
                }).then(res => {
                    for (let i = 0; i < this.attachments.length; i++) {
                        if(this.attachments[i].id === id) {
                            this.attachments.splice(i, 1);
                        }
                    }
                    console.log(res.url) //testing
                })
            }
        },
        getPriorityColor(priority) {
            if (priority === 'Urgent') {
                return 'red';
            } else if (priority === 'High') {
                return 'orange';
            } else if (priority === 'Medium') {
                return 'blue';
            } else if (priority === 'Low') {
                return 'grey';
            }
        },
        chopString(string, length) {
            if (string.length > length) {
                return `${string.slice(0, length)}...`;
            } else {
                return string;
            }
        },
    }
}
</script>

<style scoped>
table {
    text-align: left;
    padding-left: 0.5vw;
}

.dd {
    width: 10vw;
    height: 4vh;
    margin-bottom: 1.2vh;
    margin-left: 1vw;
    margin-right: 1vw;
    background-color: azure;

}

.title {
    font-size: 2.5vh;
    font-weight: bold;
    background-color: transparent;
    color: rgb(28, 140, 201);
}

.close {
    color: whitesmoke;
    background-color: transparent;
    width: 2vw;
    font-size: 1.5vw;
    font-weight: bold;
    text-align: center;
    cursor: pointer;
}

.close:hover {
    color: rgb(240, 0, 0);
}

.To {
    height: 4vh;
    border-radius: 5vh;
    border: 0.2vh solid black;
    padding-left: 0.5vw;
    margin-bottom: 1.2vh;
    margin-right: 13vw;
    width: 50vw;
    background-color: azure;
}

.Priority {
    height: 4.5vh;
    border-radius: 5vh;
    border: 0.3vh solid black;
    padding-left: 0.5vw;
    width: 7vw;
    background-color: azure;
    font-weight: bold;
    cursor: pointer;
}

.red {
    color: red;
}

.orange {
    color: orange;
}

.blue {
    color: blue;
}

.grey {
    color: grey;
}

.subject {
    height: 4.5vh;
    border-radius: 5vh;
    border: 0.2vh solid black;
    padding-left: 0.5vw;
    margin-bottom: 1.2vh;
    width: 70vw;
    background-color: azure;
}

.mailBody {
    border-radius: 1vh;
    border: 0.2vh solid black;
    padding-left: 1vw;
    padding-top: 1vh;
    margin-bottom: 1vh;
    height: 50vh;
    width: 75vw;
    text-align: right top;
    resize: none;
    background-color: azure;
}

.compose {
    background-color: #282525;
    height: 85.1vh;
    width: 80vw;
    margin: 0;
    border-bottom: 0.1vw solid black;
}

.attach {
    width: 6vw;
    height: 2.5vw;
    font-size: 2.7vh;
    margin-left: 0.5vw;
    margin-right: 0.5vw;
    cursor: pointer;
    margin-bottom: 0.5vh;
    border-radius: 4vh;
    background-color: rgb(217, 225, 225);
}

.attach:hover {
    color: rgb(7, 45, 11);
    background-color: rgb(131, 129, 129);
}

.send {
    position: relative;
    left: 45vw;
    width: 6vw;
    height: 2.5vw;
    font-size: 2.7vh;
    cursor: pointer;
    margin-bottom: 0.5vh;
    border-radius: 4vh;
    background-color: rgb(217, 225, 225);
}

.send:hover {
    color: rgb(7, 45, 11);
    background-color: rgb(131, 129, 129);
}

.attchList {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    margin-bottom: 1vh;
    margin-left: 1vw;
    width: 15vw;
    height: 15vh;
    overflow-y: scroll;
    padding: 0.2vw;
}

.Attachment {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    width: 13.5vw;
    height: 3vh;
    margin-bottom: 0.5vh;
    border-radius: 4vh;
    background-color: #9d9d9d;
}

.Attachment:hover {
    background-color: #5e5e5e;
    color: white;
}

.attchName {
    width: 10vw;
    height: 2.2vh;
    padding-left: 0.5vw;
    font-size: 2vh;
    font-weight: bold;
    overflow: hidden;
    cursor: pointer;
    color: black;
}

.attchName:hover {
    color: white;
}

.attchRemove {
    position: relative;
    left: 1.2vw;
    font-size: 2vh;
    font-weight: bold;
    cursor: pointer;
    background-color: transparent;
    border: none;
}

.attchRemove:hover {
    color: rgb(255, 0, 0);
}

.Attachment .attchRemove {
    display: none;
}

.Attachment:hover .attchRemove {
    display: block;
}

.downBar {
    display: flex;
    flex-direction: row;
    align-items: center;
    background-color: transparent;
    height: 16vh;
    width: 100%;
}

::-webkit-scrollbar {
    width: 0.8vw;
    background-color: #817f7f;
    /* Dark background */
    border-radius: 5px;
}

::-webkit-scrollbar-thumb {
    background-color: #423e3e;
    /* Lighter handle */
    border-radius: 5px;
}
</style>
