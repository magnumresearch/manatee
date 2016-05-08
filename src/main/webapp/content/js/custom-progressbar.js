function intialProgressbar(barID, progressNum, progressText) {
    $(barID).html("");
    var circle = new ProgressBar.Circle(barID, {
        color: '#37c282',
        strokeWidth: 7,
        trailColor: '#b1b7be',
        trailWidth: 3,
        duration: 1000,
        text: {
            value: '0',

        },
        step: function(state, bar) {
            bar.setText(progressText);
        }
    });
    circle.animate(progressNum, function() {
    })
}
