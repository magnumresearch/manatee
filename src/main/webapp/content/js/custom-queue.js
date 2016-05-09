// $(document).ready(function() {
//     console.log("dfadsfadsfasdfasdfasdfasdf");
//     $( ".connectedSortable" ).sortable({
//       connectWith: ".connectedSortable",
//       // distance: 1,
//       opacity: 0.5,
//       revert: 400,
//       // update: function (event, ui) {
//       //       var data = $(this).sortable('serialize');
//       //       console.log(data);
//       //   }
//       receive: function(event, ui) {
//         var id = $(ui.item).attr('id');
//         var teamID = this.id;
//         // $('#QueueController').scope().updateTeam(id, teamID);
//       },
//     }).disableSelection();


//   });

function activatejQueryUI() {
  $( ".connectedSortable" ).sortable({
      connectWith: ".connectedSortable",
      items: "tr",
      opacity: 0.5,
      revert: 400,
      receive: function(event, ui) {
        var id = $(ui.item).attr('id');
        var teamID = this.id;
        if (id=="potentialdischarge-tr") {
          ui.sender.sortable("cancel");
          return;
        }
        if (teamID=="potentialdischarge") {
          ui.sender.sortable("cancel");
          $('#QueueController').scope().updateStatus(id, "potentialdischarge");
        } else {
          $('#QueueController').scope().updateTeam(id, teamID);
        }
        // console.log(id +"  receive: "+ teamID);
      },
    }).disableSelection();
}

setInterval(refresh_queue_page, 60000);
last_update_timestamp = new Date().getTime();

function refresh_queue_page() {
  $('#QueueController').scope().addMessage();
  // new_update_timestamp = new Date().getTime();
  // if (isRepeat) {      
  //   if (new_update_timestamp-last_update_timestamp<60) {
  //     last_update_timestamp = new_update_timestamp;
  //     console.log("isRepeat");
  //     console.log(isRepeat);
  //     console.log(last_update_timestamp);
  //   } else {
  //     $('#QueueController').scope().addMessage();
  //     console.log("isRepeat");
  //     console.log(isRepeat);
  //     console.log(last_update_timestamp);
  //   }
  // } else {
  //   last_update_timestamp = new_update_timestamp;
  // }
    
}

