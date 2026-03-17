const slides = [...document.querySelectorAll(".slide")];
const progressBars = [...document.querySelectorAll(".bar")];
const counter = document.getElementById("counter");
const prevBtn = document.getElementById("prevBtn");
const nextBtn = document.getElementById("nextBtn");
const slideImages = [...document.querySelectorAll(".slide-image")];
const imageLightbox = document.getElementById("imageLightbox");
const lightboxImage = document.getElementById("lightboxImage");
const lightboxClose = document.getElementById("lightboxClose");

let index = 0;

function render() {
    slides.forEach((slide, i) => {
        slide.classList.toggle("active", i === index);
    });

    const progress = ((index + 1) / slides.length) * 100;
    progressBars.forEach((bar) => {
        bar.style.width = `${progress}%`;
    });

    counter.textContent = `${index + 1} / ${slides.length}`;
    prevBtn.disabled = index === 0;
    nextBtn.disabled = index === slides.length - 1;
    prevBtn.style.opacity = prevBtn.disabled ? "0.5" : "1";
    nextBtn.style.opacity = nextBtn.disabled ? "0.5" : "1";
}

function nextSlide() {
    index = Math.min(index + 1, slides.length - 1);
    render();
}

function prevSlide() {
    index = Math.max(index - 1, 0);
    render();
}

function openLightbox(sourceImage) {
    lightboxImage.src = sourceImage.src;
    lightboxImage.alt = sourceImage.alt || "Expanded slide image";
    imageLightbox.classList.add("open");
    imageLightbox.setAttribute("aria-hidden", "false");
}

function closeLightbox() {
    imageLightbox.classList.remove("open");
    imageLightbox.setAttribute("aria-hidden", "true");
    lightboxImage.src = "";
}

nextBtn.addEventListener("click", nextSlide);
prevBtn.addEventListener("click", prevSlide);

slideImages.forEach((image) => {
    image.addEventListener("click", () => openLightbox(image));
});

lightboxClose.addEventListener("click", closeLightbox);

imageLightbox.addEventListener("click", (event) => {
    if (event.target === imageLightbox) {
        closeLightbox();
    }
});

window.addEventListener("keydown", (event) => {
    if (event.key === "Escape" && imageLightbox.classList.contains("open")) {
        event.preventDefault();
        closeLightbox();
        return;
    }

    if (imageLightbox.classList.contains("open")) {
        return;
    }

    if (["ArrowRight", "PageDown", " "].includes(event.key)) {
        event.preventDefault();
        nextSlide();
    }

    if (["ArrowLeft", "PageUp"].includes(event.key)) {
        event.preventDefault();
        prevSlide();
    }
});

render();