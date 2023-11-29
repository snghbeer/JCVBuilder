<script>
export default {
  props: {
    title: {
      type: String,
      required: true
    },
    index: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      editedTitle: this.title,
      isEditing: false
    };
  },
  methods: {
    toggleEdit() {
      this.isEditing = !this.isEditing;
      if (this.isEditing) {
        this.$nextTick(() => {
          this.$refs.input.focus(); // Use $nextTick to ensure the input is available
        });
      }
    },
    updateTitle() {
      this.$emit('update:title', this.index, this.editedTitle);
    }
  }
};
</script>

<template>
  <div>
    <input
      v-if="isEditing"
      ref="input"
      v-model="editedTitle"
      @blur="toggleEdit"
      @input="updateTitle"
    />
    <h3 v-else @click="toggleEdit">{{ editedTitle }}</h3>
  </div>
</template>

<style scoped>
.sectionHeaderInput {
  cursor: pointer;
}
</style>
