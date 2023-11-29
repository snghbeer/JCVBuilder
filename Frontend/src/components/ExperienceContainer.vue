<script setup>
import { ref } from 'vue';
import AddBtn from './addButton.vue'
import ExperienceInput from './ExperienceInput.vue';

const props = defineProps({
 experienceList: Array,
});

//const experienceList = ref([]);

// Function to add a new experience to the list
const addExperience = () => {
  props.experienceList.push({
    startMonthYear: '',
    endMonthYear: '',
    jobDescription: '',
  });
};

</script>

<template>
    <div class="experienceContainer">
        <ExperienceInput v-for="(experience, index) in props.experienceList" :key="index" :experience="experience" />
    </div>
    <div class="flex-Center">
        <AddBtn @click="addExperience" />
    </div>

</template>

<style scoped>

.experienceContainer{
    overflow-y: auto;
    max-height: 30vh;
    padding: 1.5rem;
}

</style>