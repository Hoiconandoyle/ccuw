<template>
    <div class="viewMail">
        <div class="viewMailHeader">
            <button class="pi pi-arrow-left backBtn" @click="$emit('closeViewMail')"></button>
            <span class="viewMailHeaderSubject">{{ email.subject }}</span>
        </div>
        <div class="viewMailBody">
            <div class="viewMailBodyHeader">
                <div class="viewMailBodyHeaderLeft">
                    <span class="viewMailBodyHeaderFrom">From: {{ email.from }}</span>
                    <span class="viewMailBodyHeaderTo">To: {{ email.to }}</span>
                </div>
                <div class="viewMailBodyHeaderRight">
                    <span class="viewMailBodyHeaderPriority">{{ email.priority }}</span>
                    <span class="viewMailBodyHeaderDate">{{ email.date }}</span>
                </div>
            </div>
            <div class="viewMailBodyBody">
                <span class="viewMailBodyBodyText" v-for="line in fixBody(email.body)" :key="line">{{ line }}</span>
            </div>
            <div class="viewMailBodyFooter">
                <i class="viewMailBodyFooterTitle">Attachments:</i>
                <div class="viewMailBodyFooterAttachmentList">
                    <button v-for="attachment in email.attachments" :key="attachment"
                        class="viewMailBodyFooterAttachmentBtn" @click="download(attachment.id)">
                        {{ attachment.fileName }}
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'ViewMail',
    props: ['email', 'useremail', 'currentFolder'],
    methods: {
        download(id) {
            console.log(id);
            //let folderName= 'sent' // probs current folder must come here 
            console.log(this.currentFolder.toLowerCase())
            fetch('http://localhost:8080/view/' + this.useremail + '/' + this.email.id + '/' + this.currentFolder.toLowerCase() + '/' + id, {
                method: "GET"
            }).then(res => {
                console.log("downloaded successfully") //testing
                window.open(res.url)
            })
        },
        fixBody(body) {
            let x = [];
            let currentString = '';
            let spaceBuffer = ' '.repeat(165);

            body.split('\n').forEach((line) => {
                if (line === '') {
                    x.push(spaceBuffer);
                } else {
                    currentString = line;
                    while (currentString.length > 0) {
                        if (currentString.length <= 165) {
                            x.push(currentString);
                            currentString = '';
                        } else {
                            let pos = currentString.lastIndexOf(' ', 165);
                            if (pos === -1) pos = 165;
                            x.push(currentString.substring(0, pos));
                            currentString = currentString.substring(pos + 1);
                        }
                    }
                }
            });

            return x;
        }
    },
};
</script>

<style scoped>
.viewMail {
    display: flex;
    flex-direction: column;
    height: 85vh;
    width: 80vw;
    background-color: rgba(240, 253, 253, 0.821);
    border: 0.1vw solid black;
}

.viewMailHeader {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    height: 5%;
    width: 100%;
    background-color: rgba(0, 0, 0, 0.629);
    border-bottom-left-radius: 0.5vw;
    border-bottom-right-radius: 0.5vw;
    padding-left: 0.2vw;
    border-bottom: 0.2vw solid black;
}

.viewMailHeaderSubject {
    font-size: 2.8vh;
    font-weight: bold;
    color: white;
    margin: auto;
}

.backBtn {
    background-color: transparent;
    border: none;
    color: white;
    font-size: 2.5vh;
    font-weight: bold;
    cursor: pointer;
}

.backBtn:hover {
    color: black;
}

.viewMailBody {
    display: flex;
    flex-direction: column;
    height: 95%;
    width: 100%;
}

.viewMailBodyHeader {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    height: 10%;
    width: 100%;
    border-bottom: 0.2vw solid black;
    border-bottom-left-radius: 0.5vw;
    border-bottom-right-radius: 0.5vw;
}

.viewMailBodyHeaderLeft {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: flex-start;
    height: 100%;
    width: 80%;
}

.viewMailBodyHeaderFrom {
    color: black;
    padding: 1vh;
    font-size: 2.4vh;
    font-weight: bold;
}

.viewMailBodyHeaderTo {
    color: black;
    padding: 1vh;
    font-size: 2.4vh;
    font-weight: bold;
}

.viewMailBodyHeaderRight {
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    align-items: flex-end;
    height: 100%;
    width: 20%;
}

.viewMailBodyHeaderPriority {
    color: black;
    font-size: 2.3vh;
    font-weight: bold;
    margin-right: 1vw;
    position: relative;
    top: -25%;
}

.viewMailBodyHeaderDate {
    color: black;
    font-size: 2vh;
    font-weight: bold;
    margin-right: 1vw;
}

.viewMailBodyBody {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    height: 75%;
    width: 100%;
    background-color: azure;
    padding: 1vw;
    overflow-y: auto;
}

::-webkit-scrollbar {
    width: 0.5vw;
    background-color: #ffffff;
}

::-webkit-scrollbar-thumb {
    background-color: #acdadd;
    border-radius: 3px;
}

.viewMailBodyBodyText {
    font-size: 2vh;
    color: black;
    white-space: pre-wrap;
}

.viewMailBodyFooter {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    height: 15%;
    width: 100%;
    background-color: white;
    padding: 0.5vw;
    border-top: 0.2vw solid black;
    border-top-left-radius: 0.5vw;
    border-top-right-radius: 0.5vw;
}

.viewMailBodyFooterTitle {
    color: black;
    font-size: 2.3vh;
    font-weight: bold;
}

.viewMailBodyFooterAttachmentList {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    height: 100%;
    width: 100%;
    overflow-x: auto;
}

.viewMailBodyFooterAttachmentBtn {
    border-radius: 0.5vw;
    color: #024a97;
    font-size: 2.5vh;
    cursor: pointer;
    margin-right: 0.5vw;
}

.viewMailBodyFooterAttachmentBtn:hover {
    color: #0a3d91;
}
</style>
