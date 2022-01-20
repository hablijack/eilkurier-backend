var iso = new Isotope('.collumns', {
    itemSelector: '.collumn',
    percentPosition: true,
    masonry: {
        // use outer width of grid-sizer for columnWidth
        columnWidth: '.collumn-small'
    }
});
