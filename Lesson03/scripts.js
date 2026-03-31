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
    
    // Update page numbers highlighting
    updatePageNumbers();
}

function goToSlide(slideNumber) {
    if (slideNumber >= 1 && slideNumber <= slides.length) {
        index = slideNumber - 1;
        render();
        // Clear the input field
        const pageInput = document.getElementById('pageInput');
        if (pageInput) pageInput.value = '';
    }
}

function generatePageNumbers() {
    const pageNumbers = document.getElementById('pageNumbers');
    if (!pageNumbers) return;
    
    pageNumbers.innerHTML = '';
    
    // Show first few pages, current page area, and last few pages
    const totalSlides = slides.length;
    const current = index + 1;
    const pagesToShow = [];
    
    // Always show first page
    if (totalSlides > 0) pagesToShow.push(1);
    
    // Show pages around current
    for (let i = Math.max(2, current - 1); i <= Math.min(totalSlides - 1, current + 1); i++) {
        if (!pagesToShow.includes(i)) pagesToShow.push(i);
    }
    
    // Always show last page if more than 1 slide
    if (totalSlides > 1 && !pagesToShow.includes(totalSlides)) {
        pagesToShow.push(totalSlides);
    }
    
    // Create buttons
    pagesToShow.sort((a, b) => a - b);
    
    pagesToShow.forEach((pageNum, idx) => {
        // Add ellipsis if there's a gap
        if (idx > 0 && pageNum - pagesToShow[idx - 1] > 1) {
            const ellipsis = document.createElement('span');
            ellipsis.textContent = '...';
            ellipsis.className = 'page-ellipsis';
            pageNumbers.appendChild(ellipsis);
        }
        
        const button = document.createElement('button');
        button.textContent = pageNum;
        button.className = 'page-number';
        if (pageNum === current) {
            button.classList.add('active');
        }
        button.onclick = () => goToSlide(pageNum);
        pageNumbers.appendChild(button);
    });
}

function updatePageNumbers() {
    generatePageNumbers();
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

// Initialize page navigation and render
generatePageNumbers();
render();