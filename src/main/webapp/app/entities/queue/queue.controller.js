(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('QueueController', QueueController);

    QueueController.$inject = ['$scope', '$state', 'Queue', 'ChatService', 'Team'];

    function QueueController ($scope, $state, Queue, ChatService, Team) {
        var vm = this;
        $scope.queues = [];

        function get_max_for_today(one_team) {
            var weekdays = ['sunday','monday','tuesday','wednesday','thursday','friday','saturday'];
            var d = new Date();
            var n = d.getDay();
            if (one_team[weekdays[n]])
                return one_team[weekdays[n]]
            else
                return one_team['maxPatients']
        }

        $scope.loadAll = function() {

            var arrayTeam = [];
            var arrayPatientTeam = [];
            var arrayPotentialDischargedPatient = [];
            Team.query(function(result) {
                for (var i in result) {                    
                    if(typeof result[i] ==="object")
                        if ('name' in result[i]) {
                            arrayTeam.push({'id':result[i]['id'], 'name': result[i]['name'], 'space': get_max_for_today(result[i]), 'progressbarid':'progressbar-'+result[i]['id'] });
                            // arrayTeam.push({'id':result[i]['id'], 'name': result[i]['name'], 'space': result[i]['maxPatients'], 'progressbarid':'progressbar-'+result[i]['id'] });
                            arrayPatientTeam.push([]);
                            // console.log(result[i]['name']);
                            // console.log(get_max_for_today(result[i]));
                        }
                }  
                
                Queue.query(function(result) {
                    for (var i in result) {
                        if(typeof result[i] ==="object")
                            if ('team' in result[i]) 
                                if ('name' in result[i]['team']) {
                                    for (var j in arrayTeam) {
                                        if (arrayTeam[j]['name'] ==result[i]['team']['name']) {
                                            var tmp = result[i];
                                            // console.log(tmp);
                                            // // check if name equals empty
                                            // if(tmp['patient'] && tmp['patient']['name']=='') {
                                            //     tmp['patient']['name']=' ';
                                            // }
                                            if (result[i]['timestampInitial']) {
                                                var initialDate = result[i]['timestampInitial'];
                                                tmp['timestampSince'] = new Date(initialDate).getTime();
                                            }
                                            if (result[i]['timestampFinal']) {
                                                var finalDate = result[i]['timestampFinal'];
                                                tmp['timestampDue'] = new Date(finalDate).getTime();
                                            } else {
                                                tmp['timestampDue'] = -1;
                                            }
                                            if (result[i]['status'] && result[i]['status']=="potentialdischarge") {
                                                tmp['status'] = 1;
                                                arrayPotentialDischargedPatient.push(tmp);
                                            } else {
                                                tmp['status'] = 0;
                                            }
                                            
                                            arrayPatientTeam[j].push(tmp);
                                        }
                                    }
                                }
                    }
                    for (var i in arrayTeam) {
                        // console.log(arrayTeam[i]['space']);
                        if (arrayTeam[i]['space']==null) {
                            arrayTeam[i]['occupation'] = 0;
                            arrayTeam[i]['progressbarText'] = arrayPatientTeam[i].length +"";
                        } else if (arrayTeam[i]['space']<0) {
                            arrayTeam[i]['occupation'] = 0;
                            arrayTeam[i]['progressbarText'] = arrayPatientTeam[i].length +"";
                        } else if (arrayTeam[i]['space']==0) {
                            arrayTeam[i]['occupation'] = 0;
                            arrayTeam[i]['progressbarText'] = arrayPatientTeam[i].length +"/0";
                        } else {
                            arrayTeam[i]['occupation'] = arrayPatientTeam[i].length/arrayTeam[i]['space'];
                            arrayTeam[i]['progressbarText'] = arrayPatientTeam[i].length +"/"+arrayTeam[i]['space'];
                        }
                    }
                    $scope.teams = arrayTeam;
                    $scope.arrayPatientTeam = arrayPatientTeam;
                    $scope.arrayPotentialDischargedPatient = arrayPotentialDischargedPatient;
                    
                });
            });
        };

        $scope.loadAll();

        $scope.addMessage = function(message) {
            ChatService.send("send test message");
        };

        ChatService.receive().then(null, null, function(message) {
            // console.log("receive test message");
            // refresh_queue_page(false);
            $scope.loadAll(function(result) {
                $scope.activateProgressBar();
            });
        });

        $scope.updateTeam = function(queueID, teamID) {
            console.log("queueID, teamID:"+ queueID+"|"+ teamID);
            Queue.get({id: queueID}, function(queueResult) {
                
                Team.get({id : teamID}, function(teamResult) {
                    // console.log(teamResult);
                    queueResult.team=teamResult;
                    queueResult.timestampInitial = new Date();
                    // console.log(queueResult);
                    Queue.update(queueResult, onSaveFinished);
                });
            });
        }
        $scope.updateStatus = function(queueID, status) {
            Queue.get({id: queueID}, function(queueResult) {
                queueResult.status=status;
                Queue.update(queueResult, onSaveFinished);
            });
        }

        var onSaveFinished = function () {
            $scope.addMessage();
        };

        $scope.activateJQueryUI = function() {
            activatejQueryUI();
        }
        $scope.activateProgressBar = function(barID, progressNum, progressText) {
            intialProgressbar('#'+barID, progressNum, progressText);
        }

        $scope.recoverFromPotentialDischarge = function(queueID) {
            Queue.get({id: queueID}, function(queueResult) {
                queueResult.status='';
                Queue.update(queueResult, onSaveFinished);
            });
        }
        $scope.removeFromPotentialDischarge = function(queueID) {
            console.log(queueID);
            Queue.delete({id: queueID},
                function () {
                    $scope.addMessage();
                });

            // Queue.get({
            //     id: queueID
            // }, function(result) {
            //     console.log(result);
            //     $scope.queue = result;
            //     $('#deleteQueueConfirmation').modal('show');
            // });
        };
        $scope.delete = function(id) {
            Queue.get({
                id: id
            }, function(result) {
                $scope.queue = result;
                $('#deleteQueueConfirmation').modal('show');
            });
        };
        
    }
})();
