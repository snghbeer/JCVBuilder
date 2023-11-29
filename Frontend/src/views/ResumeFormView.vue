<script setup>
import { ref } from 'vue';
import router from '../router/index'
import { RouterLink } from 'vue-router';
import { countries } from '../assets/countries';
import { host } from '../config';

import ExperienceContainer from '../components/ExperienceContainer.vue';
import SkillContainer from '../components/SkillContainer.vue';
import SkillInput from '../components/SkillInput.vue';
import SkillSectionComponent from '../components/SkillSectionComponent.vue';

const fullName = ref('');
const email = ref('');
const phoneNumber = ref('');
const country = ref('');
const birthDate = ref('');
const experienceList = ref([]);
const skillsList = ref([]);
const isLoading = ref(false);


const generateResume = async () => {
    isLoading.value = true;
    try {
        const payload = {
        fullName: fullName.value,
        email: email.value,
        phoneNumber: phoneNumber.value,
        country: country.value,
        birthDate: birthDate.value,
        experience: experienceList.value,
        skills: skillsList.value
    }
    const response = await fetch(`${host}/generate`, {
        headers: {
            "Content-Type": "application/json"
        },
        method: "POST",
        body: JSON.stringify(payload)
    })

    if (response.ok) {
        const blob = await response.blob();
        const filename = getFilenameFromContentDisposition(response.headers.get("content-disposition"));
        const link = document.createElement("a");

        link.href = window.URL.createObjectURL(blob);
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } else {
        console.error("Failed to generate PDF:", response.statusText);
    }
    } catch (error) {
        console.error('Error generating PDF:', error);
    } finally {
        isLoading.value = false;
    }
};



const getFilenameFromContentDisposition = (contentDisposition) => {
    const match = contentDisposition && contentDisposition.match(/filename="(.+)"/);
    return match ? match[1] : "generated.pdf";
};

const updateTitle = (index, newTitle) => {
    const aCopyList = [...skillsList.value]; // Create a shallow copy of the array
    aCopyList[index].title = newTitle;
    skillsList.value = aCopyList; // Update the original ref with the modified copy
};



</script>

<template>
    <main class="formView">
        <div>
            <form @submit.prevent="generateResume">
                <div>
                    <h2 class="sectionHeader">Personal information</h2>

                    <div class="form-row">
                        <div class="form-column">
                            <label for="fullName" class="input-label">Full Name</label>
                            <input type="text" id="fullName" v-model="fullName" required />
                        </div>

                        <div class="form-column">
                            <label for="email" class="input-label">Email</label>
                            <input type="email" id="email" v-model="email" required />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-column">
                            <label for="phone" class="input-label">Phone Number</label>
                            <input type="tel" id="phone" v-model="phoneNumber" required />
                        </div>

                        <div class="form-column">
                            <label for="country" class="input-label">Country</label>
                            <select id="country" v-model="country" required>
                                <option v-for="countryOption in countries" :key="countryOption.code"
                                    :value="countryOption.code">
                                    {{ countryOption.name }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-column">
                            <label for="birthdate" class="input-label">Birth Date</label>
                            <input type="date" id="birthdate" v-model="birthDate" required />
                        </div>
                    </div>
                </div>

                <div>
                    <h2 class="sectionHeader">Professional experience</h2>
                    <ExperienceContainer :experienceList="experienceList" />

                </div>
                <div style="padding-bottom: 10px;">
                    <h2 class="sectionHeader">Skills</h2>
                    <!-- <SkillSectionComponent/> -->
                    <SkillContainer :skillSectionList="skillsList" :update="updateTitle" />

                </div>
                <div class="flex-Center">
                    <button type="submit" @click="generateResume" :disabled="isLoading">
                        {{ isLoading ? 'Generating...' : 'Generate Resume' }}
                    </button>
                    <div v-if="isLoading" class="spinner"></div>
                </div>
            </form>
        </div>

    </main>
</template>

<style scoped>
.formView {
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.4);
    width: 60vw;
    height: 70vh;
    color: black;
    overflow-y: auto;
}

.form-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15px;
}

.form-column {
    flex: 1;
    margin-right: 10px;
}

.input-label {
    min-width: 120px;
    display: inline-block;
}

.sectionHeader {
    padding-bottom: 0.85rem;
    font-weight: bold;
}

.spinner {
    border: 4px solid rgba(0, 0, 0, 0.1);
    border-radius: 50%;
    border-top: 4px solid #3498db;
    width: 20px;
    height: 20px;
    animation: spin 1s linear infinite;
    margin-left: 10px;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
</style>